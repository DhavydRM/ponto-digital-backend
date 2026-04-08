package com.dhavyd.login.dto;

import java.time.LocalDateTime;

public record RegistroUpdateDTO(LocalDateTime entrada, LocalDateTime saida, String observacao) {
}
