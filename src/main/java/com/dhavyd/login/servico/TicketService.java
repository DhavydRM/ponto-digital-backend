package com.dhavyd.login.servico;

import com.dhavyd.login.dto.InputTicket;
import com.dhavyd.login.dto.TicketDTO;
import com.dhavyd.login.entidades.Registro;
import com.dhavyd.login.entidades.Ticket;
import com.dhavyd.login.entidades.Usuario;
import com.dhavyd.login.entidades.enums.StatusTicket;
import com.dhavyd.login.entidades.enums.Tipo;
import com.dhavyd.login.entidades.enums.TurnoNumber;
import com.dhavyd.login.repositorios.RegistroRepository;
import com.dhavyd.login.repositorios.TicketRepository;
import com.dhavyd.login.repositorios.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;


@Service
@AllArgsConstructor
public class TicketService {

    private TicketRepository repository;
    private UsuarioRepository usuarioRepository;
    private RegistroRepository registroRepository;

    public TicketDTO criarTicket(InputTicket ticket) {
        Usuario usuario = usuarioRepository.findById(ticket.funcionarioId())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

        Ticket newTicket = new Ticket();
        newTicket.setNovoHorario(LocalDateTime.of(ticket.data(), ticket.hora()));
        newTicket.setTipoRegistro(ticket.tipo());
        newTicket.setMotivoTroca(ticket.motivo());
        newTicket.setStatus(StatusTicket.PENDENTE);
        newTicket.setTurnoNumber(ticket.turno());
        newTicket.setCreatedAt(LocalDateTime.now());
        newTicket.setFuncionario(usuario);
        Registro registroVinculado = linkarRegistroToTicket(ticket);
        newTicket.setRegistroRelacionado(registroVinculado);
        atualizaHorarioAtual(newTicket);

        Ticket ticketCriado = repository.save(newTicket);
        registroVinculado.setTicket(ticketCriado);
        registroRepository.save(registroVinculado);

        return TicketDTO.fromEntity(ticketCriado);
    }

    public TicketDTO buscarPorId(Long id) {
        return TicketDTO.fromEntity(repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado")));
    }

    public List<TicketDTO> buscarTodos() {
        return repository.findAll().stream()
                .map(TicketDTO::fromEntity)
                .toList();
    }

    public List<TicketDTO> buscarPorFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId).stream()
                .map(TicketDTO::fromEntity)
                .toList();
    }

    public List<TicketDTO> buscarPorStatus(StatusTicket status) {
        return repository.findByStatus(status).stream()
                .map(TicketDTO::fromEntity)
                .toList();
    }

    public TicketDTO atualizarTicket(Long id, LocalDateTime registro, Tipo tipo, String motivo) {
        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado"));

        if (ticket.getStatus() != StatusTicket.PENDENTE) {
            throw new IllegalStateException("Não é possível editar um ticket já processado");
        }

        ticket.setNovoHorario(registro);
        ticket.setTipoRegistro(tipo);
        ticket.setMotivoTroca(motivo);

        return TicketDTO.fromEntity(repository.save(ticket));
    }

    public TicketDTO aprovarTicket(Long id) {
        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado"));

        ticket.setStatus(StatusTicket.APROVADO);
        atualizaHorarioDoRegistro(ticket);
        registroRepository.save(ticket.getRegistroRelacionado());
        return TicketDTO.fromEntity(repository.save(ticket));
    }

    public TicketDTO rejeitarTicket(Long id) {
        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado"));

        ticket.setStatus(StatusTicket.REJEITADO);
        return TicketDTO.fromEntity(repository.save(ticket));
    }

    public void removerTicket(Long id) {
        repository.deleteById(id);
    }

    //Funções privadas
    private Registro linkarRegistroToTicket(InputTicket ticket) {
        LocalDateTime inicioBusca = LocalDateTime.of(ticket.data(), LocalTime.of(0, 0, 0));
        LocalDateTime finalBusca = LocalDateTime.of(ticket.data(), LocalTime.MAX);
        List<Registro> registros = registroRepository.findByPeriodoPorUser(
                inicioBusca, finalBusca, ticket.funcionarioId()
        );

        registros.sort(Comparator.comparing(Registro::getId)); //Ordena do menor para maior

        if (ticket.turno() == TurnoNumber.TURNO1) {
            return registros.getFirst();
        }

        if (ticket.turno() == TurnoNumber.TURNO2) {
            return registros.getLast();
        }

        return null;
    }

    private void atualizaHorarioAtual(Ticket ticket) {
        if (ticket.getTipoRegistro() == Tipo.ENTRADA) {
            ticket.setHorarioAtual(ticket.getRegistroRelacionado().getEntrada());
            return;
        }

        if (ticket.getTipoRegistro() == Tipo.SAIDA) {
            ticket.setHorarioAtual(ticket.getRegistroRelacionado().getSaida());
        }
    }

    private void atualizaHorarioDoRegistro(Ticket ticket) {
        Registro registro = ticket.getRegistroRelacionado();
        if (ticket.getTipoRegistro() == Tipo.ENTRADA) {
            registro.setEntrada(ticket.getNovoHorario());
            return;
        }

        if (ticket.getTipoRegistro() == Tipo.SAIDA) {
            registro.setSaida(ticket.getNovoHorario());
        }
    }
}
