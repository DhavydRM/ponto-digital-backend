package com.dhavyd.login.dto;

public record ResumoFaltasDTO(
        Long usuarioId,
        String nome,
        int totalFaltas,
        int justificadas,
        int ignoradas,
        int pendentes) {
}
