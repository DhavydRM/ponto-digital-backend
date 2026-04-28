package com.dhavyd.login.servico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dhavyd.login.dto.ResolverOcorrenciaDTO;
import com.dhavyd.login.dto.SaldoBancoHorasDTO;
import com.dhavyd.login.entidades.EscalaTrabalho;
import com.dhavyd.login.entidades.OcorrenciaAusencia;
import com.dhavyd.login.entidades.Registro;
import com.dhavyd.login.entidades.Usuario;
import com.dhavyd.login.entidades.enums.StatusOcorrencia;
import com.dhavyd.login.repositorios.EscalaTrabalhoRepository;
import com.dhavyd.login.repositorios.OcorrenciaAusenciaRepository;
import com.dhavyd.login.repositorios.RegistroRepository;
import com.dhavyd.login.repositorios.UsuarioRepository;
import com.dhavyd.login.servico.execoes.RecursoNaoEncontrado;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OcorrenciaService {

    private static final long TAMANHO_MAXIMO = 5 * 1024 * 1024;
    private static final List<String> TIPOS_PERMITIDOS = List.of(
            "application/pdf", "image/jpeg", "image/jpg", "image/png");

    private OcorrenciaAusenciaRepository ocorrenciaRepository;
    private UsuarioRepository usuarioRepository;
    private RegistroRepository registroRepository;
    private EscalaTrabalhoRepository escalaRepository;

    public List<OcorrenciaAusencia> listarTodos() {
        return ocorrenciaRepository.findAll();
    }

    public List<OcorrenciaAusencia> listarPendentes() {
        return ocorrenciaRepository.findByStatus(StatusOcorrencia.PENDENTE);
    }

    public OcorrenciaAusencia buscarPorId(Long id) {
        return ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Ocorrência não encontrada!"));
    }

    @Transactional
    public OcorrenciaAusencia resolver(Long id, ResolverOcorrenciaDTO dto) {
        OcorrenciaAusencia ocorrencia = buscarPorId(id);
        Usuario resolvidoPor = usuarioRepository.findById(dto.resolvidoPorId())
                .orElseThrow(() -> new RecursoNaoEncontrado("Usuário responsável não encontrado!"));

        ocorrencia.setStatus(dto.status());
        ocorrencia.setObservacaoGestor(dto.observacaoGestor());
        ocorrencia.setResolvidoPor(resolvidoPor);
        ocorrencia.setResolvidoEm(LocalDateTime.now());

        return ocorrenciaRepository.save(ocorrencia);
    }

    @Transactional
    public String salvarDocumento(Long ocorrenciaId, MultipartFile arquivo) throws IOException {
        buscarPorId(ocorrenciaId);

        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException("Arquivo excede o tamanho máximo de 5MB.");
        }

        String contentType = arquivo.getContentType();
        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType)) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido. Use PDF, JPG, JPEG ou PNG.");
        }

        String nomeOriginal = arquivo.getOriginalFilename();
        String nomeUnico = ocorrenciaId + "_" + nomeOriginal;
        Path destino = Paths.get("./uploads/atestados", nomeUnico);

        Files.createDirectories(destino.getParent());
        Files.write(destino, arquivo.getBytes());

        OcorrenciaAusencia ocorrencia = buscarPorId(ocorrenciaId);
        ocorrencia.setDocumentoPath(destino.toString());
        ocorrenciaRepository.save(ocorrencia);

        return destino.toString();
    }

    public byte[] baixarDocumento(Long ocorrenciaId) throws IOException {
        OcorrenciaAusencia ocorrencia = buscarPorId(ocorrenciaId);
        if (ocorrencia.getDocumentoPath() == null) {
            throw new RecursoNaoEncontrado("Documento não encontrado!");
        }
        return Files.readAllBytes(Paths.get(ocorrencia.getDocumentoPath()));
    }

    public Resource obterResource(Long ocorrenciaId) throws IOException {
        byte[] bytes = baixarDocumento(ocorrenciaId);
        return new ByteArrayResource(bytes);
    }

    // TODO: BANCO DE HORAS — ativar endpoint quando módulo for liberado
    // public SaldoBancoHorasDTO calcularSaldoBancoHoras(Long usuarioId, LocalDate inicio, LocalDate fim) {
    public SaldoBancoHorasDTO calcularSaldoBancoHoras(Long usuarioId, LocalDate inicio, LocalDate fim) {
        List<EscalaTrabalho> escalas = escalaRepository.findByUsuarioIdAndAtivoTrue(usuarioId);
        List<OcorrenciaAusencia> ocorrencias = ocorrenciaRepository
                .findByUsuarioIdAndDataBetween(usuarioId, inicio, fim);

        double horasEsperadas = 0;
        double horasRegistradas = 0;
        double horasJustificadas = 0;
        double horasIgnoradas = 0;

        for (EscalaTrabalho escala : escalas) {
            long minutos = Duration.between(escala.getHoraEntrada(), escala.getHoraSaida()).toMinutes();
            horasEsperadas += minutos / 60.0;
        }

        List<Registro> registros = registroRepository.findByPeriodoPorUser(
                inicio.atStartOfDay(), fim.atTime(LocalTime.MAX), usuarioId);

        for (Registro r : registros) {
            if (r.getEntrada() != null && r.getSaida() != null) {
                long minutos = Duration.between(r.getEntrada(), r.getSaida()).toMinutes();
                horasRegistradas += minutos / 60.0;
            }
        }

        for (OcorrenciaAusencia o : ocorrencias) {
            long minutos = Duration.between(o.getTurnoEsperadoInicio(), o.getTurnoEsperadoFim()).toMinutes();
            double horas = minutos / 60.0;
            if (o.getStatus() == StatusOcorrencia.JUSTIFICADO_ATESTADO) {
                horasJustificadas += horas;
            } else if (o.getStatus() == StatusOcorrencia.IGNORADO) {
                horasIgnoradas += horas;
            }
        }

        double horasDescontadas = horasEsperadas - horasRegistradas - horasJustificadas - horasIgnoradas;
        double saldoFinal = horasRegistradas - horasEsperadas;

        return new SaldoBancoHorasDTO(
                horasEsperadas, horasRegistradas, horasJustificadas,
                horasIgnoradas, horasDescontadas, saldoFinal);
    }
}
