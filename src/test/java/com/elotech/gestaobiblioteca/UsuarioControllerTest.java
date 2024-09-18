package com.elotech.gestaobiblioteca;

import com.elotech.gestaobiblioteca.Controller.UsuarioController;
import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoUsuario;
import com.elotech.gestaobiblioteca.DTO.DadosUsuarioCadastro;
import com.elotech.gestaobiblioteca.Service.UsuarioService;
import com.elotech.gestaobiblioteca.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @Test
    void deveCriarUsuario() {
        DadosUsuarioCadastro dadosCadastro = new DadosUsuarioCadastro(null, "João Silva", "joao@teste.com", LocalDate.now(), "999999999");
        Usuario usuario = new Usuario();
        when(usuarioService.criarUsuario(dadosCadastro)).thenReturn(usuario);
        ResponseEntity<Usuario> response = usuarioController.criarUsuario(dadosCadastro);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(usuarioService, times(1)).criarUsuario(dadosCadastro);
    }

    @Test
    void deveAtualizarUsuario() {
        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "Maria Silva", "maria@teste.com", LocalDate.now(), "888888888");
        Usuario usuario = new Usuario();
        when(usuarioService.atualizarUsuario(dadosAtualizacao)).thenReturn(usuario);
        ResponseEntity<Usuario> response = usuarioController.atualizarUsuario(dadosAtualizacao);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(usuarioService, times(1)).atualizarUsuario(dadosAtualizacao);
    }

    @Test
    void deveBuscarUsuarioPeloId() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        when(usuarioService.buscarUsuarioPeloId(id)).thenReturn(usuario);
        ResponseEntity<Usuario> response = usuarioController.buscarUsuarioPeloId(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(usuarioService, times(1)).buscarUsuarioPeloId(id);
    }

    @Test
    void deveListarUsuarios() {
        List<Usuario> usuarios = List.of(new Usuario());
        when(usuarioService.listarUsuarios()).thenReturn(usuarios);
        ResponseEntity<List<Usuario>> response = usuarioController.listarUsuarios();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(usuarioService, times(1)).listarUsuarios();
    }

    @Test
    void deveDeletarUsuario() {
        Long id = 1L;
        doNothing().when(usuarioService).deletarUsuario(id);
        ResponseEntity<Void> response = usuarioController.deletarUsuario(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioService, times(1)).deletarUsuario(id);
    }
}
