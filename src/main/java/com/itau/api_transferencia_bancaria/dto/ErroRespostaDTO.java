package com.itau.api_transferencia_bancaria.dto;

import java.time.LocalDateTime;

public record ErroRespostaDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {}
