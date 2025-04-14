package com.itau.api_transferencia_bancaria.dto;

public enum EnumStatusTransferencia
{
    SUCESSO,
    SALDO_INSUFICIENTE,
    VALOR_EXCEDENTE,
    MESMA_CONTA,
    CONCORRENCIA,
    ERRO
}
