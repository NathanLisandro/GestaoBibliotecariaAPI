package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.Controller.LivroController;
import com.elotech.gestaobiblioteca.DTO.DadosLivroCadastro;
import com.elotech.gestaobiblioteca.Service.LivroService;
import com.elotech.gestaobiblioteca.model.Livros;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroService livroService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarLivros_DeveRetornarListaDeLivros() throws Exception {
        Livros livro = new Livros();
        livro.setId(1L);
        livro.setTitulo("Título");
        when(livroService.listarLivros()).thenReturn(List.of(livro));
        mockMvc.perform(get("/livros")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L)).andExpect(jsonPath("$[0].titulo").value("Título"));
        verify(livroService, times(1)).listarLivros();
    }

    @Test
    void cadastrarLivro_DeveRetornarLivroCriado() throws Exception {
        DadosLivroCadastro dados = new DadosLivroCadastro(null, "Título", "Autor", "ISBN", LocalDate.now(), "Categoria");
        Livros livro = new Livros(dados);
        livro.setId(123L);
        when(livroService.cadastrarLivro(any(DadosLivroCadastro.class))).thenReturn(livro);
        mockMvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(livro.getId()))
                .andExpect(jsonPath("$.titulo").value(livro.getTitulo()))
                .andExpect(jsonPath("$.autor").value(livro.getAutor()))
                .andExpect(jsonPath("$.isbn").value(livro.getIsbn()))
                .andExpect(jsonPath("$.categoria").value(livro.getCategoria()));
        verify(livroService, times(1)).cadastrarLivro(any(DadosLivroCadastro.class));
    }



    @Test
    void deletarLivro_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/livros/1")).andExpect(status().isNoContent());
        verify(livroService, times(1)).deletarLivro(1L);
    }

    @Test
    void atualizarLivro_DeveRetornarLivroAtualizado() throws Exception {
        DadosLivroCadastro dados = new DadosLivroCadastro(1L, "Novo Título", "Novo Autor", "Novo ISBN", LocalDate.now(), "Nova Categoria");
        Livros livro = new Livros(dados);
        when(livroService.atualizarLivros(any())).thenReturn(livro);
        mockMvc.perform(put("/livros").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Novo Título"));
        verify(livroService, times(1)).atualizarLivros(any());
    }
}
