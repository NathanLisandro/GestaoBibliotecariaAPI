package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.Exeptions.ErroDetalhe;
import com.elotech.gestaobiblioteca.Exeptions.GlobalExceptions;
import com.elotech.gestaobiblioteca.Exeptions.IsbnExistenteException;
import com.elotech.gestaobiblioteca.Exeptions.ItemNaoEncontradoException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionsTest {

    private final GlobalExceptions globalExceptions = new GlobalExceptions();

    @Test
    void handleItemNaoEncontradoException() {
        ItemNaoEncontradoException ex = new ItemNaoEncontradoException("Item não encontrado");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("URI=/teste");
        ResponseEntity<ErroDetalhe> response = globalExceptions.handleItemNaoEncontradoException(ex, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Item não encontrado", response.getBody().getMensagem());
        assertEquals(List.of("URI=/teste"), response.getBody().getDetalhes());
    }

    @Test
    void handleValidationExceptions() {
        var bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "campo1", "Erro no campo1"));
        bindingResult.addError(new FieldError("objectName", "campo2", "Erro no campo2"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<ErroDetalhe> response = globalExceptions.handleValidationExceptions(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de validação", response.getBody().getMensagem());
        assertEquals(2, response.getBody().getDetalhes().size());
    }

    @Test
    void handleConstraintViolationException() {
        ConstraintViolation<String> violation = mock(ConstraintViolation.class);
        Path propertyPath = mock(Path.class);
        when(propertyPath.toString()).thenReturn("campo1");
        when(violation.getPropertyPath()).thenReturn(propertyPath);
        when(violation.getMessage()).thenReturn("Erro no campo1");
        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
        WebRequest request = mock(WebRequest.class);
        ResponseEntity<ErroDetalhe> response = globalExceptions.handleConstraintViolationException(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de validação de parâmetros", response.getBody().getMensagem());
        assertEquals(1, response.getBody().getDetalhes().size());
    }
    @Test
    void handleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Violação de integridade");
        WebRequest request = mock(WebRequest.class);
        ResponseEntity<ErroDetalhe> response = globalExceptions.handleDataIntegrityViolationException(ex, request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Violação de integridade de dados", response.getBody().getMensagem());
    }

    @Test
    void handleMethodNotAllowed() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("URI=/teste");
        ResponseEntity<ErroDetalhe> response = globalExceptions.handleMethodNotAllowed(ex, request);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    void handleHttpMessageNotReadableException() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Mensagem não legível");
        WebRequest request = mock(WebRequest.class);
        ResponseEntity<ErroDetalhe> response = globalExceptions.handleHttpMessageNotReadableException(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Formato de mensagem HTTP inválido", response.getBody().getMensagem());
    }

    @Test
    void handleAllExceptions() {
        Exception ex = new Exception("Erro genérico");
        WebRequest request = mock(WebRequest.class);
        ResponseEntity<ErroDetalhe> response = globalExceptions.handleAllExceptions(ex, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno do servidor", response.getBody().getMensagem());
    }

    @Test
    void handleIsbnExistenteException() {
        IsbnExistenteException ex = new IsbnExistenteException("ISBN já existe");
        WebRequest request = mock(WebRequest.class);
        ResponseEntity<ErroDetalhe> response = globalExceptions.handleIsbnExistenteException(ex, request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("ISBN já existe", response.getBody().getMensagem());
    }
}
