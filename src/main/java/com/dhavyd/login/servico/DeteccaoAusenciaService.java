package com.dhavyd.login.servico;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhavyd.login.entidades.EscalaTrabalho;
import com.dhavyd.login.entidades.OcorrenciaAusencia;
import com.dhavyd.login.entidades.Registro;
import com.dhavyd.login.entidades.Usuario;
import com.dhavyd.login.entidades.enums.StatusOcorrencia;
import com.dhavyd.login.repositorios.EscalaTrabalhoRepository;
import com.dhavyd.login.repositorios.OcorrenciaAusenciaRepository;
import com.dhavyd.login.repositorios.RegistroRepository;
import com.dhavyd.login.repositorios.UsuarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeteccaoAusenciaService {

    private UsuarioRepository usuarioRepository;
    private EscalaTrabalhoRepository escalaRepository;
    private RegistroRepository registroRepository;
    private OcorrenciaAusenciaRepository ocorrenciaRepository;

    @Transactional
    public int detectarParaData(LocalDate data) {
        DayOfWeek diaSemana = data.getDayOfWeek();
        List<Usuario> usuariosAtivos = usuarioRepository.findByAtivo(true);
        int criadas = 0;

        for (Usuario usuario : usuariosAtivos) {
            List<EscalaTrabalho> escalasDoDia = escalaRepository
                    .findByUsuarioIdAndAtivoTrue(usuario.getId())
                    .stream()
                    .filter(e -> e.getDiaSemana() == diaSemana)
                    .toList();

            for (EscalaTrabalho escala : escalasDoDia) {
                boolean presente = verificarPresenca(usuario.getId(), data, escala);

                if (!presente) {
                    boolean jaExiste = ocorrenciaRepository
                            .existsByUsuarioIdAndDataAndTurnoEsperadoInicio(
                                    usuario.getId(), data, escala.getHoraEntrada());

                    if (!jaExiste) {
                        OcorrenciaAusencia ocorrencia = new OcorrenciaAusencia();
                        ocorrencia.setUsuario(usuario);
                        ocorrencia.setData(data);
                        ocorrencia.setTurnoEsperadoInicio(escala.getHoraEntrada());
                        ocorrencia.setTurnoEsperadoFim(escala.getHoraSaida());
                        ocorrencia.setStatus(StatusOcorrencia.PENDENTE);
                        ocorrencia.setCriadoEm(LocalDateTime.now());
                        ocorrenciaRepository.save(ocorrencia);
                        criadas++;
                    }
                }
            }
        }

        return criadas;
    }

    @Transactional
    public int detectarIntervalo(LocalDate de, LocalDate ate) {
        int total = 0;
        LocalDate current = de;
        while (!current.isAfter(ate)) {
            total += detectarParaData(current);
            current = current.plusDays(1);
        }
        return total;
    }

    private boolean verificarPresenca(Long usuarioId, LocalDate data, EscalaTrabalho escala) {
        LocalTime horaEntrada = escala.getHoraEntrada();
        LocalTime horaSaida = escala.getHoraSaida();

        LocalDateTime janelaInicio;
        LocalDateTime janelaFim;

        if (horaEntrada.isBefore(LocalTime.NOON)) {
            janelaInicio = data.atTime(horaEntrada).minusMinutes(30);
            janelaFim = data.atTime(LocalTime.of(12, 30));
        } else {
            janelaInicio = data.atTime(LocalTime.of(13, 30));
            janelaFim = data.atTime(horaSaida).plusMinutes(30);
        }

        List<Registro> registros = registroRepository.findByPeriodoPorUser(
                janelaInicio, janelaFim, usuarioId);

        return registros.stream()
                .anyMatch(r -> r.getEntrada() != null && r.getSaida() != null);
    }
}
