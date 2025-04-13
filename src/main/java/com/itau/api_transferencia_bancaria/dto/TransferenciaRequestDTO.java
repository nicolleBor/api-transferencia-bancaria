package com.itau.api_transferencia_bancaria.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransferenciaRequestDTO {
    private String contaOrigem;
    private String contaDestino;
    private Double valor;
}
