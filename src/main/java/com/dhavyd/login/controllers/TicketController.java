package com.dhavyd.login.controllers;

import com.dhavyd.login.dto.InputTicket;
import com.dhavyd.login.dto.TicketDTO;
import com.dhavyd.login.entidades.enums.StatusTicket;
import com.dhavyd.login.servico.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

    private TicketService service;

    @PostMapping
    public ResponseEntity<TicketDTO> criarTicket(@RequestBody InputTicket ticket) {
        TicketDTO created = service.criarTicket(ticket);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> buscarTodos(
            @RequestParam(name = "funcionarioId", required = false) Long funcionarioId,
            @RequestParam(name = "status", required = false) StatusTicket status) {
        List<TicketDTO> tickets;

        if (funcionarioId != null) {
            tickets = service.buscarPorFuncionario(funcionarioId);
        } else if (status != null) {
            tickets = service.buscarPorStatus(status);
        } else {
            tickets = service.buscarTodos();
        }

        return ResponseEntity.ok().body(tickets);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TicketDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TicketDTO> atualizarTicket(@PathVariable Long id,
                                                     @RequestBody InputTicket ticket) {
        TicketDTO updated = service.atualizarTicket(id, LocalDateTime.of(ticket.data(), ticket.hora()), ticket.tipo(), ticket.motivo());
        return ResponseEntity.ok().body(updated);
    }

    @PutMapping(value = "/{id}/aprovar")
    public ResponseEntity<TicketDTO> aprovarTicket(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.aprovarTicket(id));
    }

    @PutMapping(value = "/{id}/rejeitar")
    public ResponseEntity<TicketDTO> rejeitarTicket(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.rejeitarTicket(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarTicket(@PathVariable Long id) {
        service.removerTicket(id);
        return ResponseEntity.noContent().build();
    }
}
