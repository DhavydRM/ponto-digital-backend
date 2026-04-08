package com.dhavyd.login.dto;

import com.dhavyd.login.entidades.enums.Tipo;
import com.dhavyd.login.entidades.enums.TurnoNumber;

import java.time.LocalDate;
import java.time.LocalTime;

public record InputTicket(
        LocalDate data,
        LocalTime hora,
        Tipo tipo,
        TurnoNumber turno,
        String motivo,
        Long funcionarioId
) {}
