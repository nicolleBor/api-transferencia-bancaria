package com.itau.api_transferencia_bancaria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HttpResponseCodeDTO {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}