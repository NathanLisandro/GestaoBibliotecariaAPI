package com.elotech.gestaobiblioteca.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DadosAtualizacaoLivro
        (@NotNull(message = "Para atualização, o id não pode ser nulo")
         Long id,
         String titulo,
         String autor,
         String isbn,
         @JsonFormat(pattern = "dd/MM/yyyy")
         LocalDate data_publicacao,
         String categoria)
{}
