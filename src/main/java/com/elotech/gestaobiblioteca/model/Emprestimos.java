package com.elotech.gestaobiblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity(name = "emprestimos")
@Table(name = "emprestimos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livro_id", nullable= false)
    private Livros livro;
    private LocalDate data_emprestimo;
    private LocalDate data_devolucao;
    private String status;
}
