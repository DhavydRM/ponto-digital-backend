package com.dhavyd.login.servico;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dhavyd.login.dto.HorarioIncomumDTO;
import com.dhavyd.login.entidades.EscalaTrabalho;
import com.dhavyd.login.entidades.Registro;
import com.dhavyd.login.entidades.enums.TipoHorarioIncomum;
import com.dhavyd.login.repositorios.EscalaTrabalhoRepository;
import com.dhavyd.login.repositorios.RegistroRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HorarioIncomumService {

    private static final long TOLERANCIA_MINUTOS = 15;
    private static final double LIMIAR_CURTO = 0.70;
    private static final double LIMIAR_LONGO = 1.30;

    private RegistroRepository registroRepository;
    private EscalaTrabalhoRepository escalaRepository;

    public List<HorarioIncomumDTO> analisar(Long usuarioId, LocalDate inicio, LocalDate fim) {
        List<Registro> registros = (usuarioId != null)
                ? registroRepository.findByPeriodoPorUser(
                        inicio.atStartOfDay(), fim.atTime(LocalTime.MAX), usuarioId)
                : registroRepository.findByPeriodo(
                        inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));

        // pré-carrega todas as escalas ativas, agrupadas por usuarioId
        List<EscalaTrabalho> todasEscalas = escalaRepository.findByAtivoTrue();
        Map<Long, List<EscalaTrabalho>> escalasPorUsuario = todasEscalas.stream()
                .collect(Collectors.groupingBy(e -> e.getUsuario().getId()));

        return registros.stream()
                .filter(r -> r.getEntrada() != null && r.getSaida() != null)
                .flatMap(r -> analisarRegistro(r, escalasPorUsuario).stream())
                .toList();
    }

    private List<HorarioIncomumDTO> analisarRegistro(
            Registro r, Map<Long, List<EscalaTrabalho>> escalasPorUsuario) {

        Long uid = r.getUsuario().getId();
        LocalDate data = r.getEntrada().toLocalDate();
        DayOfWeek dow = data.getDayOfWeek();

        List<EscalaTrabalho> escalasDoUsuario = escalasPorUsuario.getOrDefault(uid, List.of());
        List<EscalaTrabalho> escalasNoDia = escalasDoUsuario.stream()
                .filter(e -> e.getDiaSemana() == dow)
                .toList();

        if (escalasNoDia.isEmpty()) return List.of();

        EscalaTrabalho escala = escalasNoDia.get(0);
        LocalTime entradaReal = r.getEntrada().toLocalTime();
        LocalTime saidaReal = r.getSaida().toLocalTime();
        LocalTime entradaEsperada = escala.getHoraEntrada();
        LocalTime saidaEsperada = escala.getHoraSaida();

        List<HorarioIncomumDTO> resultado = new ArrayList<>();

        long diffEntrada = ChronoUnit.MINUTES.between(entradaEsperada, entradaReal);
        long diffSaida = ChronoUnit.MINUTES.between(saidaEsperada, saidaReal);

        if (diffEntrada < -TOLERANCIA_MINUTOS) {
            resultado.add(build(r, TipoHorarioIncomum.ENTRADA_ANTECIPADA,
                    "Entrada " + Math.abs(diffEntrada) + " min antes do esperado (" + entradaEsperada + ")",
                    Math.abs(diffEntrada)));
        } else if (diffEntrada > TOLERANCIA_MINUTOS) {
            resultado.add(build(r, TipoHorarioIncomum.ENTRADA_ATRASADA,
                    "Entrada " + diffEntrada + " min após o esperado (" + entradaEsperada + ")",
                    diffEntrada));
        }

        if (diffSaida < -TOLERANCIA_MINUTOS) {
            resultado.add(build(r, TipoHorarioIncomum.SAIDA_ANTECIPADA,
                    "Saída " + Math.abs(diffSaida) + " min antes do esperado (" + saidaEsperada + ")",
                    Math.abs(diffSaida)));
        } else if (diffSaida > TOLERANCIA_MINUTOS) {
            resultado.add(build(r, TipoHorarioIncomum.SAIDA_ATRASADA,
                    "Saída " + diffSaida + " min após o esperado (" + saidaEsperada + ")",
                    diffSaida));
        }

        long duracaoReal = ChronoUnit.MINUTES.between(entradaReal, saidaReal);
        long duracaoEsperada = ChronoUnit.MINUTES.between(entradaEsperada, saidaEsperada);

        if (duracaoEsperada > 0) {
            double ratio = (double) duracaoReal / duracaoEsperada;
            if (ratio < LIMIAR_CURTO) {
                long desvio = duracaoEsperada - duracaoReal;
                resultado.add(build(r, TipoHorarioIncomum.TURNO_CURTO,
                        "Turno " + desvio + " min mais curto que o esperado", desvio));
            } else if (ratio > LIMIAR_LONGO) {
                long desvio = duracaoReal - duracaoEsperada;
                resultado.add(build(r, TipoHorarioIncomum.TURNO_LONGO,
                        "Turno " + desvio + " min mais longo que o esperado", desvio));
            }
        }

        return resultado;
    }

    private HorarioIncomumDTO build(Registro r, TipoHorarioIncomum tipo, String descricao, long desvio) {
        return new HorarioIncomumDTO(
                r.getId(),
                r.getUsuario().getId(),
                r.getUsuario().getNome(),
                r.getEntrada().toLocalDate(),
                tipo,
                descricao,
                desvio);
    }
}
