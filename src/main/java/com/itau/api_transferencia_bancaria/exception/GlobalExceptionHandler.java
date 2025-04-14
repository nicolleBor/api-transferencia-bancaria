package com.itau.api_transferencia_bancaria.exception;

import com.itau.api_transferencia_bancaria.dto.HttpResponseCodeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<HttpResponseCodeDTO> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ContaJaExisteException.class)
    public ResponseEntity<HttpResponseCodeDTO> handleContaJaExiste(ContaJaExisteException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<HttpResponseCodeDTO> handleOperacaoNaoPermitida(OperacaoNaoPermitidaException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<HttpResponseCodeDTO> handleSaldoInsuficiente(SaldoInsuficienteException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferenciaInvalidaException.class)
    public ResponseEntity<HttpResponseCodeDTO> handleTransferenciaInvalida(TransferenciaInvalidaException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(CorpoRequisicaoException.class)
    public ResponseEntity<HttpResponseCodeDTO> handleCorpoRequisicaoInvalido(CorpoRequisicaoException ex) {
        return buildResponse("Corpo da requisição inválido ou malformado.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<HttpResponseCodeDTO> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return buildResponse("Recurso ou endpoint não encontrado.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MetodoNaoSuportadoException.class)
    public ResponseEntity<HttpResponseCodeDTO> handleMetodoNaoSuportado(MetodoNaoSuportadoException ex) {
        return buildResponse("Método HTTP não suportado para este endpoint.", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponseCodeDTO> handleException(Exception ex) {
        return buildResponse("Erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConcorrenciaException.class)
    public ResponseEntity<String> handleConcorrencia(ConcorrenciaException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }

    private ResponseEntity<HttpResponseCodeDTO> buildResponse(String message, HttpStatus status) {
        HttpResponseCodeDTO error = new HttpResponseCodeDTO(
                status.value(),
                message,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(error);
    }
}
