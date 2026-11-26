package com.curso.petshop.comum;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiError> tratarNaoEncontrado(
            RecursoNaoEncontradoException exception,
            HttpServletRequest request) {
        return resposta(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request,
                Map.of());
    }

    @ExceptionHandler({
            RecursoDuplicadoException.class,
            ExclusaoNaoPermitidaException.class,
            DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> tratarConflito(
            RuntimeException exception,
            HttpServletRequest request) {
        String mensagem = exception instanceof DataIntegrityViolationException
                ? "A operação viola uma regra de integridade"
                : exception.getMessage();
        return resposta(HttpStatus.CONFLICT, mensagem, request, Map.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> tratarParametroInvalido(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro " + exception.getName(),
                request,
                Map.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> tratarRegraInvalida(
            IllegalArgumentException exception,
            HttpServletRequest request) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request,
                Map.of());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> tratarOperacaoNaoPermitida(
            IllegalStateException exception,
            HttpServletRequest request) {
        return resposta(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request,
                Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                campos.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()));

        return resposta(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos são inválidos",
                request,
                campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> tratarJsonInvalido(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        return resposta(
                HttpStatus.BAD_REQUEST,
                "JSON ausente ou inválido",
                request,
                Map.of());
    }

    private ResponseEntity<ApiError> resposta(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request,
            Map<String, String> campos) {
        ApiError erro = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI(),
                campos);
        return ResponseEntity.status(status).body(erro);
    }
}
