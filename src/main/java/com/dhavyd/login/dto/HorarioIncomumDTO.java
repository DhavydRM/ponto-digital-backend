package com.dhavyd.login.dto;

import com.dhavyd.login.entidades.enums.TipoHorarioIncomum;
import java.time.LocalDate;

public record HorarioIncomumDTO(
        Long registroId,
        Long usuarioId,
        String nomeUsuario,
        LocalDate data,
        TipoHorarioIncomum tipo,
        String descricao,
        long minutosDesvio) {
}
