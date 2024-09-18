package com.elotech.gestaobiblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record DadosUsuarioCadastro(Long id,
                                   @NotBlank(message = "O nome é obrigatório.")
                                   String nome,
                                   @NotBlank(message = "O email é obrigatório.")
                                   @Email(message = "Email inválido.")
                                   String email,
                                   @NotNull(message = "A data de cadastro é obrigatória.")
                                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                   @PastOrPresent(message = "A data de cadastro não pode ser no futuro.")
                                   LocalDate dataCadastro,
                                   @NotBlank(message = "O telefone é obrigatório.")
                                   String telefone
) {
}

