package com.itau.api_transferencia_bancaria.model;

import com.itau.api_transferencia_bancaria.dto.EnumStatusTransferencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "clienteOrigem")
    private Cliente clienteOrigem;
    @ManyToOne
    @JoinColumn(name = "clienteDestino")
    private Cliente clienteDestino;
    private Double valor;
    private LocalDateTime data;
    @Enumerated(EnumType.STRING)
    private EnumStatusTransferencia status;

    public Transferencia(Cliente clienteOrigem, Cliente clienteDestino, Double valor, EnumStatusTransferencia status) {
        this.clienteOrigem = clienteOrigem;
        this.clienteDestino = clienteDestino;
        this.valor = valor;
        this.status = status;
        this.data = LocalDateTime.now();
    }
}
