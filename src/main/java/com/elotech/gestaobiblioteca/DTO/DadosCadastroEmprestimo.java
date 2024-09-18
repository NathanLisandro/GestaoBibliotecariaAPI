package com.elotech.gestaobiblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record DadosCadastroEmprestimo(
        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId,
        @NotNull(message = "O ID do livro é obrigatório")
        Long livroId,
        @NotNull(message = "A data do empréstimo é obrigatória")
        @PastOrPresent(message = "A data não pode ser maior que a data atual")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dataEmprestimo,
        @NotNull(message = "A data de devolução é obrigatória")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        @FutureOrPresent
        LocalDate dataDevolucao,
        @NotNull(message = "O status do empréstimo é obrigatório")
        String status
) {}
