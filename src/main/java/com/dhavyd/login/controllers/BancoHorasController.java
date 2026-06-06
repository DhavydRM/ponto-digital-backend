package com.dhavyd.login.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dhavyd.login.dto.SaldoBancoHorasDTO;
import com.dhavyd.login.dto.SaldoBancoHorasResumoDTO;
import com.dhavyd.login.servico.OcorrenciaService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/banco-horas")
public class BancoHorasController {

    private OcorrenciaService ocorrenciaService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<SaldoBancoHorasDTO> saldoPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(ocorrenciaService.calcularSaldoBancoHoras(usuarioId, inicio, fim));
    }

    @GetMapping
    public ResponseEntity<List<SaldoBancoHorasResumoDTO>> saldoTodos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(ocorrenciaService.calcularSaldoTodos(inicio, fim));
    }
}
