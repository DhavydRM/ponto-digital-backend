package com.dhavyd.login.dto;

import com.dhavyd.login.entidades.Ticket;
import com.dhavyd.login.entidades.enums.StatusTicket;
import com.dhavyd.login.entidades.enums.Tipo;
import com.dhavyd.login.entidades.enums.TurnoNumber;

import java.time.LocalDateTime;

public record TicketDTO(
        Long id,
        LocalDateTime horarioAtual,
        LocalDateTime novoHorario,
        LocalDateTime createdAt,
        Tipo tipoRegistro,
        TurnoNumber turno,
        String motivoTroca,
        StatusTicket status,
        Long funcionarioId,
        String nomeFuncionario,
        Long registroId
) {
    public static TicketDTO fromEntity(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getHorarioAtual(),
                ticket.getNovoHorario(),
                ticket.getCreatedAt(),
                ticket.getTipoRegistro(),
                ticket.getTurnoNumber(),
                ticket.getMotivoTroca(),
                ticket.getStatus(),
                ticket.getFuncionario().getId(),
                ticket.getFuncionario().getNome(),
                ticket.getRegistroRelacionado().getId()
        );
    }
}
