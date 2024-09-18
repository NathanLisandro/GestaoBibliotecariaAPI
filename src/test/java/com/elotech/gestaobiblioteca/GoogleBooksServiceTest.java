package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.DTO.GoogleBookResponseDto;
import com.elotech.gestaobiblioteca.DTO.ItemDto;
import com.elotech.gestaobiblioteca.Service.GoogleBooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.eq;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class GoogleBooksServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GoogleBooksService googleBooksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarLivrosPorTitulo_deveRetornarListaDeLivros_quandoRespostaForValida() {
        String titulo = "Java";
        GoogleBookResponseDto mockResponse = new GoogleBookResponseDto(List.of(new ItemDto(null)));
        when(restTemplate.getForObject(anyString(), eq(GoogleBookResponseDto.class))).thenReturn(mockResponse);
        List<ItemDto> livros = googleBooksService.buscarLivrosPorTitulo(titulo);
        assertEquals(1, livros.size());
    }

    @Test
    void buscarLivrosPorTitulo_deveRetornarListaVazia_quandoRespostaForNula() {
        String titulo = "Java";
        when(restTemplate.getForObject(anyString(), eq(GoogleBookResponseDto.class))).thenReturn(null);
        List<ItemDto> livros = googleBooksService.buscarLivrosPorTitulo(titulo);
        assertEquals(0, livros.size());
    }

    @Test
    void buscarLivrosPorTitulo_deveRetornarListaVazia_quandoNaoExistiremResultados() {
        String titulo = "TituloQueNaoExiste";
        GoogleBookResponseDto mockResponse = new GoogleBookResponseDto(List.of());
        when(restTemplate.getForObject(anyString(), eq(GoogleBookResponseDto.class))).thenReturn(mockResponse);
        List<ItemDto> livros = googleBooksService.buscarLivrosPorTitulo(titulo);
        assertEquals(0, livros.size());
    }
}
