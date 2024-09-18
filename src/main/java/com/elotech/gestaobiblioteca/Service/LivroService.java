package com.elotech.gestaobiblioteca.Service;

import com.elotech.gestaobiblioteca.DTO.DadosLivroCadastro;
import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoLivro;
import com.elotech.gestaobiblioteca.Exeptions.EmprestimoDetectadoException;
import com.elotech.gestaobiblioteca.Exeptions.IsbnExistenteException;
import com.elotech.gestaobiblioteca.Exeptions.ItemNaoEncontradoException;
import com.elotech.gestaobiblioteca.Repository.LivroRepository;
import com.elotech.gestaobiblioteca.model.Emprestimos;
import com.elotech.gestaobiblioteca.model.Livros;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final EmprestimoService emprestimoService;

    public LivroService(LivroRepository livroRepository, EmprestimoService emprestimoService) {
        this.livroRepository = livroRepository;
        this.emprestimoService = emprestimoService;
    }

    public List<Livros> listarLivros() {
        return livroRepository.findAll();
    }

    public Livros cadastrarLivro(DadosLivroCadastro livro) {
        Livros livroMapeamento = new Livros(livro);

        if (livroRepository.existsByIsbn(livroMapeamento.getIsbn())) {
            throw new IsbnExistenteException("ISBN já existe.");
        }

        return livroRepository.save(livroMapeamento);
    }

    public Livros atualizarLivros(DadosAtualizacaoLivro livroAtualizacao) {
        Livros livroExistente = livroRepository.findById(livroAtualizacao.id()).orElseThrow(() -> new ItemNaoEncontradoException("Livro não foi encontrado"));
        Optional.ofNullable(livroAtualizacao.titulo()).ifPresent(livroExistente::setTitulo);
        Optional.ofNullable(livroAtualizacao.autor()).ifPresent(livroExistente::setAutor);
        Optional.ofNullable(livroAtualizacao.isbn()).ifPresent(livroExistente::setIsbn);
        Optional.ofNullable(livroAtualizacao.data_publicacao()).ifPresent(livroExistente::setData_publicacao);
        Optional.ofNullable(livroAtualizacao.categoria()).ifPresent(livroExistente::setCategoria);
        return livroRepository.save(livroExistente);
    }

    public void deletarLivro(Long id) {
        Livros livro = livroRepository.findById(id).orElseThrow(() -> new ItemNaoEncontradoException("O livro não foi encontrado"));
        boolean verificarEmprestimoLivro = emprestimoService.verificarEmprestimoLivro(livro.getId());
        if(!verificarEmprestimoLivro) {
            throw new EmprestimoDetectadoException("Emprestimo detectado");
        }
        livroRepository.delete(livro);

    }

    public List<Livros> recomendarLivros(Long usuarioId) {
        List<Emprestimos> emprestimosDoUsuario = emprestimoService.listarEmprestimosPeloIdUsuario(usuarioId);
        Set<String> categoriasEmprestadas = emprestimosDoUsuario.stream()
                .map(Emprestimos::getLivro)
                .map(Livros::getCategoria)
                .collect(Collectors.toSet());
        Set<Long> idsLivrosEmprestados = emprestimosDoUsuario.stream()
                .map(Emprestimos::getLivro)
                .map(Livros::getId)
                .collect(Collectors.toSet());
        return livroRepository.findByCategoriaIn(categoriasEmprestadas).stream()
                .filter(livro -> !idsLivrosEmprestados.contains(livro.getId()))
                .collect(Collectors.toList());
    }

    public Livros buscarLivro(Long id) {
        Optional<Livros> livro = livroRepository.findById(id);
        if (livro.isPresent()) {
            return livro.get();
        }
        throw new ItemNaoEncontradoException("O livro não foi encontrado");
    }

    @Transactional
    public void deletarLivroComEmprestimos(Long id) {
        Livros livro = livroRepository.findById(id)
                .orElseThrow(() -> new ItemNaoEncontradoException("Livro não foi encontrado"));
        emprestimoService.excluirEmprestimosPorLivroId(id);
        livroRepository.delete(livro);
    }
}
