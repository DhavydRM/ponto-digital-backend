package com.dhavyd.login.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dhavyd.login.dto.HorarioIncomumDTO;
import com.dhavyd.login.servico.HorarioIncomumService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/horarios-incomuns")
public class HorarioIncomumController {

    private HorarioIncomumService horarioIncomumService;

    @GetMapping
    public ResponseEntity<List<HorarioIncomumDTO>> analisar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(horarioIncomumService.analisar(usuarioId, inicio, fim));
    }
}
