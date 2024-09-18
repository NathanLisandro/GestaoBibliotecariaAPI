package com.elotech.gestaobiblioteca.model;

import com.elotech.gestaobiblioteca.DTO.DadosLivroCadastro;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
public class Livros {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titulo;
    private String autor;
    @Column(unique = true, nullable = false)
    private String isbn;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data_publicacao;
    private String categoria;

    public Livros(DadosLivroCadastro livro) {
        this.titulo = livro.titulo();
        this.autor = livro.autor();
        this.isbn = livro.isbn();
        this.data_publicacao = livro.data_publicacao();
        this.categoria = livro.categoria();

    }

}
