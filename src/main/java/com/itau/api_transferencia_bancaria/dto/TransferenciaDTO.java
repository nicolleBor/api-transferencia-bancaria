package com.itau.api_transferencia_bancaria.dto;

import com.itau.api_transferencia_bancaria.model.Transferencia;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransferenciaDTO {
    private String contaOrigem;
    private String contaDestino;
    private Double valor;
    private LocalDateTime data;
    @Enumerated(EnumType.STRING)
    private EnumStatusTransferencia status;

    public TransferenciaDTO(Transferencia transferencia){
        this.contaOrigem = transferencia.getClienteOrigem().getNumeroConta();
        this.contaDestino = transferencia.getClienteDestino().getNumeroConta();
        this.valor = transferencia.getValor();
        this.data = transferencia.getData();
        this.status = transferencia.getStatus();
    }
}
