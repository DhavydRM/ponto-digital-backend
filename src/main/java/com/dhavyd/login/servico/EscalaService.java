package com.dhavyd.login.servico;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhavyd.login.entidades.EscalaTrabalho;
import com.dhavyd.login.entidades.Usuario;
import com.dhavyd.login.entidades.enums.TipoEscala;
import com.dhavyd.login.repositorios.EscalaTrabalhoRepository;
import com.dhavyd.login.repositorios.UsuarioRepository;
import com.dhavyd.login.servico.execoes.RecursoNaoEncontrado;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EscalaService {

    private EscalaTrabalhoRepository escalaRepository;
    private UsuarioRepository usuarioRepository;

    public List<EscalaTrabalho> listarTodos() {
        return escalaRepository.findAll();
    }

    public EscalaTrabalho buscarPorId(Long id) {
        return escalaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Escala não encontrada!"));
    }

    public List<EscalaTrabalho> buscarPorUsuario(Long usuarioId) {
        return escalaRepository.findByUsuarioIdAndAtivoTrue(usuarioId);
    }

    @Transactional
    public EscalaTrabalho salvar(EscalaTrabalho escala) {
        return escalaRepository.save(escala);
    }

    @Transactional
    public EscalaTrabalho atualizar(Long id, EscalaTrabalho novosDados) {
        EscalaTrabalho existente = buscarPorId(id);
        existente.setTipoEscala(novosDados.getTipoEscala());
        existente.setDiaSemana(novosDados.getDiaSemana());
        existente.setHoraEntrada(novosDados.getHoraEntrada());
        existente.setHoraSaida(novosDados.getHoraSaida());
        existente.setAtivo(novosDados.getAtivo());
        return escalaRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        EscalaTrabalho escala = buscarPorId(id);
        escala.setAtivo(false);
        escalaRepository.save(escala);
    }

    @Transactional
    public List<EscalaTrabalho> aplicarPadrao(Long usuarioId, TipoEscala tipo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Usuário não encontrado!"));

        List<EscalaTrabalho> atuais = escalaRepository.findByUsuarioIdAndAtivoTrue(usuarioId);
        atuais.forEach(e -> e.setAtivo(false));
        escalaRepository.saveAll(atuais);

        return switch (tipo) {
            case EMPLOYEE_PADRAO -> criarPadraoEmployee(usuario);
            case EMPLOYEE_ESPECIAL -> criarEspecialEmployee(usuario);
            case INTERN_4H -> criarIntern4h(usuario);
            case INTERN_3H -> criarIntern3h(usuario);
        };
    }

    private List<EscalaTrabalho> criarPadraoEmployee(Usuario usuario) {
        return escalaRepository.saveAll(List.of(
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.MONDAY,    LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.MONDAY,    LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.TUESDAY,   LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.TUESDAY,   LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.WEDNESDAY, LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.THURSDAY,  LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.THURSDAY,  LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.FRIDAY,    LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.FRIDAY,    LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_PADRAO, DayOfWeek.SATURDAY,  LocalTime.of(8, 0),  LocalTime.of(12, 0))
        ));
    }

    private List<EscalaTrabalho> criarEspecialEmployee(Usuario usuario) {
        return escalaRepository.saveAll(List.of(
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.MONDAY,    LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.TUESDAY,   LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.TUESDAY,   LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.WEDNESDAY, LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.THURSDAY,  LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.THURSDAY,  LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.FRIDAY,    LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.FRIDAY,    LocalTime.of(14, 0), LocalTime.of(18, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.SATURDAY,  LocalTime.of(8, 0),  LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.EMPLOYEE_ESPECIAL, DayOfWeek.SATURDAY,  LocalTime.of(14, 0), LocalTime.of(18, 0))
        ));
    }

    private List<EscalaTrabalho> criarIntern4h(Usuario usuario) {
        return escalaRepository.saveAll(List.of(
            criar(usuario, TipoEscala.INTERN_4H, DayOfWeek.MONDAY,    LocalTime.of(8, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_4H, DayOfWeek.TUESDAY,   LocalTime.of(8, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_4H, DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_4H, DayOfWeek.THURSDAY,  LocalTime.of(8, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_4H, DayOfWeek.FRIDAY,    LocalTime.of(8, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_4H, DayOfWeek.SATURDAY,  LocalTime.of(8, 0), LocalTime.of(12, 0))
        ));
    }

    private List<EscalaTrabalho> criarIntern3h(Usuario usuario) {
        return escalaRepository.saveAll(List.of(
            criar(usuario, TipoEscala.INTERN_3H, DayOfWeek.MONDAY,    LocalTime.of(9, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_3H, DayOfWeek.TUESDAY,   LocalTime.of(9, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_3H, DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_3H, DayOfWeek.THURSDAY,  LocalTime.of(9, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_3H, DayOfWeek.FRIDAY,    LocalTime.of(9, 0), LocalTime.of(12, 0)),
            criar(usuario, TipoEscala.INTERN_3H, DayOfWeek.SATURDAY,  LocalTime.of(9, 0), LocalTime.of(12, 0))
        ));
    }

    private EscalaTrabalho criar(Usuario usuario, TipoEscala tipo, DayOfWeek dia, LocalTime entrada, LocalTime saida) {
        return new EscalaTrabalho(null, usuario, tipo, dia, entrada, saida, true);
    }
}
