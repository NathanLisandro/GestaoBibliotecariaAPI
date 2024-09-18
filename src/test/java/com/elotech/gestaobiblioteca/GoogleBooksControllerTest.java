package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.Controller.GoogleBooksController;
import com.elotech.gestaobiblioteca.DTO.DadosLivroCadastro;
import com.elotech.gestaobiblioteca.DTO.ItemDto;
import com.elotech.gestaobiblioteca.DTO.VolumeInfoDto;
import com.elotech.gestaobiblioteca.Service.GoogleBooksService;
import com.elotech.gestaobiblioteca.Service.LivroService;
import com.elotech.gestaobiblioteca.model.Livros;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GoogleBooksControllerTest {

    @Mock
    private GoogleBooksService googleBooksService;

    @Mock
    private LivroService livroService;

    @InjectMocks
    private GoogleBooksController googleBooksController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarLivros_deveRetornarListaDeLivros_quandoTituloForValido() {
        String titulo = "Effective Java";
        ItemDto itemDto = new ItemDto(new VolumeInfoDto("Effective Java", List.of("Joshua Bloch"), "2008-05-28", Collections.emptyList(), List.of("Programming"), "Description"));
        when(googleBooksService.buscarLivrosPorTitulo(titulo)).thenReturn(List.of(itemDto));
        ResponseEntity<List<ItemDto>> response = googleBooksController.buscarLivros(titulo);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(googleBooksService, times(1)).buscarLivrosPorTitulo(titulo);
    }

    @Test
    void adicionarLivro_deveAdicionarLivroComSucesso_quandoDadosForemValidos() {
        ItemDto itemDto = new ItemDto(new VolumeInfoDto("Effective Java", List.of("Joshua Bloch"), "2008-05-28", Collections.emptyList(), List.of("Programming"), "Description"));
        Livros livro = new Livros(new DadosLivroCadastro(null, "Effective Java", "Joshua Bloch", "123456789", LocalDate.of(2008, 5, 28), "Programming"));
        when(livroService.cadastrarLivro(any(DadosLivroCadastro.class))).thenReturn(livro);
        ResponseEntity<Livros> response = googleBooksController.adicionarLivro(itemDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Effective Java", response.getBody().getTitulo());
        verify(livroService, times(1)).cadastrarLivro(any(DadosLivroCadastro.class));
    }
}
