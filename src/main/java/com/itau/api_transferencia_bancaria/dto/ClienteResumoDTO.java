package com.itau.api_transferencia_bancaria.dto;

import com.itau.api_transferencia_bancaria.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResumoDTO {
    private Long id;
    private String nome;
    private String numeroConta;

    public ClienteResumoDTO(Cliente cliente){
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.numeroConta = cliente.getNumeroConta();
    }

}
