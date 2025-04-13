package com.itau.api_transferencia_bancaria.exception;

public class ContaJaExisteException extends RuntimeException {

    public ContaJaExisteException(String message) {
        super(message);
    }
}