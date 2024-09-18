package com.elotech.gestaobiblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record DadosAtualizacaoUsuario (
        @NotNull(message = "O id não pode ser nulo")
        Long id,
        String nome,
        @Email
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @PastOrPresent(message = "A data de cadastro não pode ser no futuro.")
        LocalDate dataCadastro,
        String telefone
) {
}
