package com.elotech.gestaobiblioteca.Service;

import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoEmprestimo;
import com.elotech.gestaobiblioteca.DTO.DadosCadastroEmprestimo;
import com.elotech.gestaobiblioteca.Exeptions.EmprestimoDetectadoException;
import com.elotech.gestaobiblioteca.Exeptions.ItemNaoEncontradoException;
import com.elotech.gestaobiblioteca.Repository.EmprestimoRepository;
import com.elotech.gestaobiblioteca.model.Emprestimos;
import com.elotech.gestaobiblioteca.model.Livros;
import com.elotech.gestaobiblioteca.model.Usuario;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service

public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioService usuarioService;
    private final LivroService livroService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,  @Lazy UsuarioService usuarioService,  @Lazy LivroService livroService) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioService = usuarioService;
        this.livroService = livroService;
;
    }


    public Page<Emprestimos> listarEmprestimos(PageRequest pageRequest) {
        return emprestimoRepository.findAll(pageRequest);
    }

    public Emprestimos criarEmprestimo(DadosCadastroEmprestimo emprestimoDTO) {
        Usuario usuario = usuarioService.buscarUsuarioPeloId(emprestimoDTO.usuarioId());
        Livros livro = livroService.buscarLivro(emprestimoDTO.livroId());
        Emprestimos emprestimo = new Emprestimos();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setData_emprestimo(emprestimoDTO.dataEmprestimo());
        emprestimo.setData_devolucao(emprestimoDTO.dataDevolucao());
        emprestimo.setStatus(emprestimoDTO.status());

        return emprestimoRepository.save(emprestimo);
    }


    public Emprestimos atualizarEmprestimo(Long id, DadosAtualizacaoEmprestimo emprestimoDTO) {
        Emprestimos emprestimoExistente = emprestimoRepository.findById(id).orElseThrow(() -> new ItemNaoEncontradoException("Empréstimo não encontrado"));
        Optional.ofNullable(emprestimoDTO.dataDevolucao()).ifPresent(emprestimoExistente::setData_devolucao);
        Optional.ofNullable(emprestimoDTO.status()).ifPresent(emprestimoExistente::setStatus);
        return emprestimoRepository.save(emprestimoExistente);
    }


    public boolean verificarEmprestimoUsuario(Usuario usuario) {
        boolean verificarEmprestimoUsuario = emprestimoRepository.findByUsuario(usuario).isEmpty();
        if (verificarEmprestimoUsuario) {
            return true;
        }
        return false;
    }

    public List<Emprestimos> listarEmprestimosPeloIdUsuario(Long id){
        Usuario usuario = usuarioService.buscarUsuarioPeloId(id);
        return emprestimoRepository.findByUsuario(usuario);
    }

    public boolean verificarEmprestimoLivro(Long id){
        return emprestimoRepository.findByLivroId(id).isEmpty();
    }

    @Transactional
    public void excluirEmprestimosPorLivroId(Long livroId) {
        List<Emprestimos> emprestimos = emprestimoRepository.findByLivroId(livroId);
        if (!emprestimos.isEmpty()) {
            emprestimoRepository.deleteAll(emprestimos);
        }
    }

    public List<Emprestimos> listarEmprestimosPeloIdLivro(Long id){
        List<Emprestimos> emprestimos = emprestimoRepository.findByLivroId(id);

        return emprestimos;
    }

}
