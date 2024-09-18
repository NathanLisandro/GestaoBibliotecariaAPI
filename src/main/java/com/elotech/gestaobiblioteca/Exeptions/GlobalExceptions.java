package com.elotech.gestaobiblioteca.Exeptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptions {


    @ExceptionHandler(ItemNaoEncontradoException.class)
    public ResponseEntity<ErroDetalhe> handleItemNaoEncontradoException(ItemNaoEncontradoException ex, WebRequest request) {
        ErroDetalhe erroDetalhe = new ErroDetalhe(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage(), List.of(request.getDescription(false)));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroDetalhe);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDetalhe> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());
        ErroDetalhe erroDetalhe = new ErroDetalhe(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Erro de validação", detalhes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroDetalhe);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroDetalhe> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> detalhes = ex.getConstraintViolations().stream().map(violation -> violation.getPropertyPath() + ": " + violation.getMessage()).collect(Collectors.toList());
        ErroDetalhe erroDetalhe = new ErroDetalhe(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Erro de validação de parâmetros", detalhes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroDetalhe);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroDetalhe> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String mensagem = ex.getMostSpecificCause().getMessage();
        ErroDetalhe erroDetalhe = new ErroDetalhe(LocalDateTime.now(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), "Violação de integridade de dados", List.of(mensagem));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroDetalhe);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroDetalhe> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        ErroDetalhe erroDetalhe = new ErroDetalhe(LocalDateTime.now(), HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), ex.getMessage(), List.of(request.getDescription(false)));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(erroDetalhe);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroDetalhe> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String mensagem = ex.getMostSpecificCause().getMessage();
        ErroDetalhe erroDetalhe = new ErroDetalhe(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Formato de mensagem HTTP inválido", List.of(mensagem));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroDetalhe);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDetalhe> handleAllExceptions(Exception ex, WebRequest request) {
        ErroDetalhe erroDetalhe = new ErroDetalhe(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Erro interno do servidor", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroDetalhe);
    }
    @ExceptionHandler(IsbnExistenteException.class)
    public ResponseEntity<ErroDetalhe> handleIsbnExistenteException(IsbnExistenteException ex, WebRequest request) {
        ErroDetalhe erroDetalhe = new ErroDetalhe(LocalDateTime.now(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), "ISBN já existe", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroDetalhe);
    }
    @ExceptionHandler(EmprestimoDetectadoException.class)
    public ResponseEntity<ErroDetalhe> handleEmprestimoDetectadoException(EmprestimoDetectadoException ex, WebRequest request) {
        ErroDetalhe erroDetalhe = new ErroDetalhe(LocalDateTime.now(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), "Empréstimo detectado", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroDetalhe);
    }
}