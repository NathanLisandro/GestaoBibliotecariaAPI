package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoUsuario;
import com.elotech.gestaobiblioteca.DTO.DadosUsuarioCadastro;
import com.elotech.gestaobiblioteca.Exeptions.ItemNaoEncontradoException;
import com.elotech.gestaobiblioteca.Repository.UsuarioRepository;
import com.elotech.gestaobiblioteca.Service.UsuarioService;
import com.elotech.gestaobiblioteca.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    void deveCriarUsuario() {
        DadosUsuarioCadastro dadosCadastro = new DadosUsuarioCadastro(null, "João Silva", "joao@teste.com", LocalDate.now(), "999999999");
        Usuario usuario = new Usuario(dadosCadastro);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        Usuario resultado = usuarioService.criarUsuario(dadosCadastro);
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioNaoEncontrado() {
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemNaoEncontradoException.class, () -> {usuarioService.deletarUsuario(id);});
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(0)).delete(any());
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "Maria Silva", "maria@teste.com", LocalDate.now(), "888888888");
        Usuario usuarioExistente = new Usuario();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(usuarioExistente)).thenReturn(usuarioExistente);
        Usuario resultado = usuarioService.atualizarUsuario(dadosAtualizacao);
        assertNotNull(resultado);
        assertEquals("Maria Silva", usuarioExistente.getNome());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioNaoEncontrado() {
        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "Maria Silva", "maria@teste.com", LocalDate.now(), "888888888");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ItemNaoEncontradoException.class, () -> {usuarioService.atualizarUsuario(dadosAtualizacao);});
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(0)).save(any());
    }

    @Test
    void deveBuscarUsuarioPeloIdComSucesso() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        Usuario resultado = usuarioService.buscarUsuarioPeloId(id);
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioNaoEncontrado() {
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemNaoEncontradoException.class, () -> {usuarioService.buscarUsuarioPeloId(id);});
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void deveListarUsuarios() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        assertEquals(1, usuarios.size());
        verify(usuarioRepository, times(1)).findAll();
    }
}
