package com.itau.api_transferencia_bancaria.exception;

public class ConcorrenciaException extends RuntimeException {
    public ConcorrenciaException(String message) {
        super(message);
    }
}
