package com.elotech.gestaobiblioteca.Service;

import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoUsuario;
import com.elotech.gestaobiblioteca.DTO.DadosUsuarioCadastro;
import com.elotech.gestaobiblioteca.Exeptions.EmprestimoDetectadoException;
import com.elotech.gestaobiblioteca.Exeptions.ItemNaoEncontradoException;
import com.elotech.gestaobiblioteca.Repository.EmprestimoRepository;
import com.elotech.gestaobiblioteca.Repository.UsuarioRepository;
import com.elotech.gestaobiblioteca.model.Emprestimos;
import com.elotech.gestaobiblioteca.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    @Autowired
    private EmprestimoService emprestimoService;
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(DadosUsuarioCadastro usuario) {
        Usuario usuarioMap = new Usuario(usuario);
        return usuarioRepository.save(usuarioMap);
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ItemNaoEncontradoException("Usuario não foi encontrado"));
        boolean verificarEmprestimo = emprestimoService.verificarEmprestimoUsuario(usuario);
        if(verificarEmprestimo){
            throw new EmprestimoDetectadoException("Emprestimo detectado");
        }

        usuarioRepository.delete(usuario);
    }

    public Usuario atualizarUsuario(DadosAtualizacaoUsuario usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioDTO.id()).orElseThrow(() -> new ItemNaoEncontradoException("Usuario não foi encontrado"));
        Optional.ofNullable(usuarioDTO.nome()).ifPresent(usuarioExistente::setNome);
        Optional.ofNullable(usuarioDTO.email()).ifPresent(usuarioExistente::setEmail);
        Optional.ofNullable(usuarioDTO.telefone()).ifPresent(usuarioExistente::setTelefone);
        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario buscarUsuarioPeloId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ItemNaoEncontradoException("Usuario não foi encontrado"));
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public void excluirUsuarioComEmprestimos(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ItemNaoEncontradoException("Usuário não foi encontrado"));
        List<Emprestimos> emprestimos = emprestimoRepository.findByUsuario(usuario);
        if (!emprestimos.isEmpty()) {
            emprestimoRepository.deleteAll(emprestimos);
        }
        usuarioRepository.delete(usuario);
    }
}
