package com.dhavyd.login.dto;

public record SaldoBancoHorasResumoDTO(
        Long usuarioId,
        String nomeUsuario,
        double horasEsperadas,
        double horasRegistradas,
        double horasJustificadas,
        double horasIgnoradas,
        double horasDescontadas,
        double saldoFinal) {

    public static SaldoBancoHorasResumoDTO from(Long id, String nome, SaldoBancoHorasDTO saldo) {
        return new SaldoBancoHorasResumoDTO(id, nome,
                saldo.horasEsperadas(), saldo.horasRegistradas(),
                saldo.horasJustificadas(), saldo.horasIgnoradas(),
                saldo.horasDescontadas(), saldo.saldoFinal());
    }
}
