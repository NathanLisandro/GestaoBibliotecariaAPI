package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoEmprestimo;
import com.elotech.gestaobiblioteca.DTO.DadosCadastroEmprestimo;
import com.elotech.gestaobiblioteca.Exeptions.ItemNaoEncontradoException;
import com.elotech.gestaobiblioteca.Repository.EmprestimoRepository;
import com.elotech.gestaobiblioteca.Repository.LivroRepository;
import com.elotech.gestaobiblioteca.Repository.UsuarioRepository;
import com.elotech.gestaobiblioteca.Service.EmprestimoService;
import com.elotech.gestaobiblioteca.model.Emprestimos;
import com.elotech.gestaobiblioteca.model.Livros;
import com.elotech.gestaobiblioteca.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EmprestimoServiceTest {

    @InjectMocks
    private EmprestimoService emprestimoService;

    @Mock
    private EmprestimoRepository emprestimoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private LivroRepository livroRepository;

    @Test
    void deveListarEmprestimos() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Emprestimos emprestimo = new Emprestimos();
        Page<Emprestimos> page = new PageImpl<>(List.of(emprestimo));
        when(emprestimoRepository.findAll(pageRequest)).thenReturn(page);
        Page<Emprestimos> result = emprestimoService.listarEmprestimos(pageRequest);
        assertEquals(1, result.getTotalElements());
        verify(emprestimoRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void deveCriarEmprestimoComSucesso() {
        DadosCadastroEmprestimo dadosCadastro = new DadosCadastroEmprestimo(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(7), "PENDENTE");
        Usuario usuario = new Usuario();
        Livros livro = new Livros();
        Emprestimos emprestimo = new Emprestimos();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(emprestimoRepository.save(any(Emprestimos.class))).thenReturn(emprestimo);
        Emprestimos result = emprestimoService.criarEmprestimo(dadosCadastro);
        assertNotNull(result);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(livroRepository, times(1)).findById(1L);
        verify(emprestimoRepository, times(1)).save(any(Emprestimos.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        DadosCadastroEmprestimo dadosCadastro = new DadosCadastroEmprestimo(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(7), "PENDENTE");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ItemNaoEncontradoException.class, () -> {emprestimoService.criarEmprestimo(dadosCadastro);});
        verify(usuarioRepository, times(1)).findById(1L);
        verifyNoInteractions(livroRepository);
        verifyNoInteractions(emprestimoRepository);
    }

    @Test
    void deveAtualizarEmprestimoComSucesso() {
        Long id = 1L;
        DadosAtualizacaoEmprestimo dadosAtualizacao = new DadosAtualizacaoEmprestimo(1L, 1L, LocalDate.now(),  "DEVOLVIDO");
        Emprestimos emprestimoExistente = new Emprestimos();
        when(emprestimoRepository.findById(id)).thenReturn(Optional.of(emprestimoExistente));
        when(emprestimoRepository.save(emprestimoExistente)).thenReturn(emprestimoExistente);
        Emprestimos result = emprestimoService.atualizarEmprestimo(id, dadosAtualizacao);
        assertNotNull(result);
        verify(emprestimoRepository, times(1)).findById(id);
        verify(emprestimoRepository, times(1)).save(emprestimoExistente);
    }

    @Test
    void deveLancarExcecaoQuandoEmprestimoNaoEncontrado() {
        Long id = 1L;
        DadosAtualizacaoEmprestimo dadosAtualizacao = new DadosAtualizacaoEmprestimo(1L, 1L, LocalDate.now(), "DEVOLVIDO");
        when(emprestimoRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemNaoEncontradoException.class, () -> {emprestimoService.atualizarEmprestimo(id, dadosAtualizacao);});
        verify(emprestimoRepository, times(1)).findById(id);
        verify(emprestimoRepository, times(0)).save(any());
    }

}
