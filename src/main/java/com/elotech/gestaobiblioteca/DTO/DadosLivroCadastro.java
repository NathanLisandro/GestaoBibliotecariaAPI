package com.elotech.gestaobiblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;


import java.time.LocalDate;

public record DadosLivroCadastro(
        Long id,
        @NotBlank(message = "Título não pode ser nulo")
        String titulo,
        @NotBlank(message = "Autor não pode ser nulo")
        String autor,
        @NotBlank(message = "ISBN não pode ser nulo")
        String isbn,
        @PastOrPresent(message = "A data de publicação deve ser no passado ou presente")
        @NotNull(message = "A data de publicação não pode ser nula")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate data_publicacao,
        @NotBlank(message = "A categoria não pode ser nula")
        String categoria
) {
}
