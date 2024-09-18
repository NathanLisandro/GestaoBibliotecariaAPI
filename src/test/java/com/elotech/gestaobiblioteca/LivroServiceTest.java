package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoLivro;
import com.elotech.gestaobiblioteca.DTO.DadosLivroCadastro;
import com.elotech.gestaobiblioteca.Exeptions.IsbnExistenteException;
import com.elotech.gestaobiblioteca.Exeptions.ItemNaoEncontradoException;
import com.elotech.gestaobiblioteca.Repository.LivroRepository;
import com.elotech.gestaobiblioteca.Service.EmprestimoService;
import com.elotech.gestaobiblioteca.Service.LivroService;
import com.elotech.gestaobiblioteca.model.Livros;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class LivroServiceTest {

    @InjectMocks
    private LivroService livroService;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private EmprestimoService emprestimoService;

    @Test
    void deveListarLivros() {
        Livros livro = new Livros();
        when(livroRepository.findAll()).thenReturn(List.of(livro));
        List<Livros> livros = livroService.listarLivros();
        assertEquals(1, livros.size());
        verify(livroRepository, times(1)).findAll();
    }

    @Test
    void deveCadastrarLivroComSucesso() {
        DadosLivroCadastro dadosCadastro = new DadosLivroCadastro(null, "Título", "Autor", "ISBN123", LocalDate.now(), "Categoria");
        Livros livro = new Livros(dadosCadastro);
        when(livroRepository.existsByIsbn("ISBN123")).thenReturn(false);
        when(livroRepository.save(any(Livros.class))).thenReturn(livro);
        Livros resultado = livroService.cadastrarLivro(dadosCadastro);
        assertNotNull(resultado);
        verify(livroRepository, times(1)).existsByIsbn("ISBN123");
        verify(livroRepository, times(1)).save(any(Livros.class));
    }

    @Test
    void deveLancarExcecaoQuandoIsbnExistir() {
        DadosLivroCadastro dadosCadastro = new DadosLivroCadastro(null, "Título", "Autor", "ISBN123", LocalDate.now(), "Categoria");
        when(livroRepository.existsByIsbn("ISBN123")).thenReturn(true);
        assertThrows(IsbnExistenteException.class, () -> {livroService.cadastrarLivro(dadosCadastro);});
        verify(livroRepository, times(1)).existsByIsbn("ISBN123");
        verify(livroRepository, times(0)).save(any(Livros.class));
    }

    @Test
    void deveAtualizarLivroComSucesso() {
        DadosAtualizacaoLivro dadosAtualizacao = new DadosAtualizacaoLivro(1L, "Novo Título", null, null, null, null);
        Livros livroExistente = new Livros();
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livroExistente));
        when(livroRepository.save(livroExistente)).thenReturn(livroExistente);
        Livros resultado = livroService.atualizarLivros(dadosAtualizacao);
        assertNotNull(resultado);
        assertEquals("Novo Título", livroExistente.getTitulo());
        verify(livroRepository, times(1)).findById(1L);
        verify(livroRepository, times(1)).save(livroExistente);
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoEncontrado() {
        DadosAtualizacaoLivro dadosAtualizacao = new DadosAtualizacaoLivro(1L, "Novo Título", null, null, null, null);
        when(livroRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ItemNaoEncontradoException.class, () -> {livroService.atualizarLivros(dadosAtualizacao);});
        verify(livroRepository, times(1)).findById(1L);
        verify(livroRepository, times(0)).save(any());
    }


    @Test
    void deveLancarExcecaoAoDeletarLivroNaoEncontrado() {
        Long id = 1L;
        when(livroRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemNaoEncontradoException.class, () -> {livroService.deletarLivro(id);});
        verify(livroRepository, times(1)).findById(id);
        verify(livroRepository, times(0)).delete(any());
    }
}
