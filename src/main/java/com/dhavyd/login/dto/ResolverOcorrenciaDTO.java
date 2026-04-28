package com.dhavyd.login.dto;

import com.dhavyd.login.entidades.enums.StatusOcorrencia;

public record ResolverOcorrenciaDTO(
    StatusOcorrencia status,
    String observacaoGestor,
    Long resolvidoPorId
) {}
