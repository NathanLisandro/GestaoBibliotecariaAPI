package com.elotech.gestaobiblioteca.model;

import com.elotech.gestaobiblioteca.DTO.DadosUsuarioCadastro;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataCadastro;

    public Usuario(DadosUsuarioCadastro usuarioDTO) {
        this.nome = usuarioDTO.nome();
        this.email = usuarioDTO.email();
        this.telefone = usuarioDTO.telefone();
        this.dataCadastro = usuarioDTO.dataCadastro();


    }
}
