package com.itau.api_transferencia_bancaria.model;

import com.itau.api_transferencia_bancaria.dto.ClienteDetalhadoDTO;
import com.itau.api_transferencia_bancaria.dto.ClienteResumoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true)
    private String numeroConta;
    private Double saldoConta;

    public Cliente(ClienteResumoDTO clienteResumoDTO){
        this.id = clienteResumoDTO.getId();
        this.nome = clienteResumoDTO.getNome();
        this.numeroConta = clienteResumoDTO.getNumeroConta();
    }

    public Cliente(ClienteDetalhadoDTO clienteDetalhadoDTO){
        this.nome = clienteDetalhadoDTO.getNome();
        this.numeroConta = clienteDetalhadoDTO.getNumeroConta();
        this.saldoConta = clienteDetalhadoDTO.getSaldoConta();
    }

}
