package com.dhavyd.login.controllers;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dhavyd.login.dto.ResolverOcorrenciaDTO;
import com.dhavyd.login.dto.ResumoFaltasDTO;
import com.dhavyd.login.entidades.OcorrenciaAusencia;
import com.dhavyd.login.servico.DeteccaoAusenciaService;
import com.dhavyd.login.servico.OcorrenciaService;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/ocorrencias")
public class OcorrenciaController {

    private OcorrenciaService service;
    private DeteccaoAusenciaService deteccaoService;

    @GetMapping
    public ResponseEntity<?> listarTodos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long usuarioId) {

        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping(value = "/pendentes")
    public ResponseEntity<?> listarPendentes() {
        return ResponseEntity.ok(service.listarPendentes());
    }

    @PutMapping(value = "/{id}/resolver")
    public ResponseEntity<OcorrenciaAusencia> resolver(
            @PathVariable Long id,
            @RequestBody ResolverOcorrenciaDTO dto) {
        return ResponseEntity.ok(service.resolver(id, dto));
    }

    @PostMapping(value = "/{id}/documento")
    public ResponseEntity<String> subirDocumento(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile arquivo) throws IOException {
        return ResponseEntity.ok(service.salvarDocumento(id, arquivo));
    }

    @GetMapping(value = "/{id}/documento")
    public ResponseEntity<Resource> baixarDocumento(@PathVariable Long id) throws IOException {
        Resource resource = service.obterResource(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping(value = "/detectar")
    public ResponseEntity<Integer> detectar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate de,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ate) {
        int total = deteccaoService.detectarIntervalo(de, ate);
        return ResponseEntity.ok(total);
    }

    @GetMapping(value = "/resumo/{usuarioId}")
    public ResponseEntity<ResumoFaltasDTO> resumoPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.resumoPorUsuario(usuarioId, inicio, fim));
    }

    @GetMapping(value = "/resumo")
    public ResponseEntity<List<ResumoFaltasDTO>> resumoTodos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.resumoTodos(inicio, fim));
    }
}
