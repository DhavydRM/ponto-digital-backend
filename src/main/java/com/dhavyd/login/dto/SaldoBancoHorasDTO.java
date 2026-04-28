package com.dhavyd.login.dto;

public record SaldoBancoHorasDTO(
    double horasEsperadas,
    double horasRegistradas,
    double horasJustificadas,
    double horasIgnoradas,
    double horasDescontadas,
    double saldoFinal
) {}
