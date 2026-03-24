package com.dhavyd.login.entidades.enums;

import java.time.LocalDateTime;
import java.util.Objects;

public enum StatusRegistro {
    ABERTO,
    FECHADO,
    UNDEFINED;

    public static StatusRegistro retornaStatus(LocalDateTime saida) {
        if (Objects.nonNull(saida)) {
            return StatusRegistro.FECHADO;
        }

        return StatusRegistro.ABERTO;
    }
}
