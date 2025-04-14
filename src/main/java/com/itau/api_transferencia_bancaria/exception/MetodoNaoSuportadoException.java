package com.itau.api_transferencia_bancaria.exception;

public class MetodoNaoSuportadoException extends RuntimeException {
    public MetodoNaoSuportadoException(String message) {
        super(message);
    }
}