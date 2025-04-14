package com.itau.api_transferencia_bancaria.dto;

import com.itau.api_transferencia_bancaria.model.Cliente;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDetalhadoDTO {
    @NotBlank(message = "Nome é obrigatório.")
    private String nome;
    private String numeroConta;
    @NotNull(message = "Saldo inicial é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Saldo inicial deve ser positivo.")
    private Double saldoConta;

    public ClienteDetalhadoDTO(Cliente cliente){
        this.nome = cliente.getNome();
        this.numeroConta = cliente.getNumeroConta();
        this.saldoConta = cliente.getSaldoConta();
    }

}