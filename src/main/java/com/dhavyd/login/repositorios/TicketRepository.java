package com.dhavyd.login.repositorios;

import com.dhavyd.login.entidades.Ticket;
import com.dhavyd.login.entidades.enums.StatusTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByFuncionarioId(Long funcionarioId);
    List<Ticket> findByStatus(StatusTicket status);
}
