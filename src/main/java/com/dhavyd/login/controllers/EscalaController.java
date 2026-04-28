package com.dhavyd.login.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dhavyd.login.entidades.EscalaTrabalho;
import com.dhavyd.login.entidades.enums.TipoEscala;
import com.dhavyd.login.servico.EscalaService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/escalas")
public class EscalaController {

    private EscalaService service;

    @GetMapping
    public ResponseEntity<List<EscalaTrabalho>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping(value = "/usuario/{id}")
    public ResponseEntity<List<EscalaTrabalho>> listarPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorUsuario(id));
    }

    @PostMapping
    public ResponseEntity<EscalaTrabalho> criar(@RequestBody EscalaTrabalho escala) {
        EscalaTrabalho criado = service.salvar(escala);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EscalaTrabalho> atualizar(@PathVariable Long id, @RequestBody EscalaTrabalho escala) {
        return ResponseEntity.ok(service.atualizar(id, escala));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/aplicar-padrao/{usuarioId}")
    public ResponseEntity<List<EscalaTrabalho>> aplicarPadrao(
            @PathVariable Long usuarioId,
            @RequestParam TipoEscala tipo) {
        List<EscalaTrabalho> escalas = service.aplicarPadrao(usuarioId, tipo);
        return ResponseEntity.ok(escalas);
    }
}
