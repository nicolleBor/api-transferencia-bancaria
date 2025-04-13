package com.itau.api_transferencia_bancaria.exception;

public class TransferenciaInvalidaException extends RuntimeException {
    public TransferenciaInvalidaException(String message) {
        super(message);
    }
}