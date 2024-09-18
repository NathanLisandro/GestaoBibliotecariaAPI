package com.elotech.gestaobiblioteca.Controller;

import com.elotech.gestaobiblioteca.DTO.DadosLivroCadastro;
import com.elotech.gestaobiblioteca.DTO.ItemDto;
import com.elotech.gestaobiblioteca.Service.GoogleBooksService;
import com.elotech.gestaobiblioteca.Service.LivroService;
import com.elotech.gestaobiblioteca.model.Livros;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/googlebooks")
@Tag(name = "Google Books", description = "Endpoints para integração com Google Books e gerenciamento de livros")
public class GoogleBooksController {

    private final GoogleBooksService googleBooksService;
    private final LivroService livroService;

    public GoogleBooksController(GoogleBooksService googleBooksService, LivroService livroService) {
        this.googleBooksService = googleBooksService;
        this.livroService = livroService;
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar livros no Google Books", description = "Busca livros no Google Books com base no título fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livros encontrados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemDto.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro500")))
    })
    public ResponseEntity<List<ItemDto>> buscarLivros(@RequestParam String titulo) {
        List<ItemDto> livros = googleBooksService.buscarLivrosPorTitulo(titulo);
        return ResponseEntity.ok(livros);
    }

    @PostMapping("/adicionar")
    @Operation(summary = "Adicionar livro ao sistema", description = "Adiciona um novo livro ao sistema com base nos dados obtidos do Google Books.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro adicionado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livros.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro500")))
    })
    public ResponseEntity<Livros> adicionarLivro(@RequestBody @Valid ItemDto item) {
        DadosLivroCadastro dadosLivro = mapearParaDadosLivroCadastro(item);
        Livros livro = livroService.cadastrarLivro(dadosLivro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livro);
    }

    private DadosLivroCadastro mapearParaDadosLivroCadastro(ItemDto item) {
        String titulo = item.volumeInfo().title();
        String autor = item.volumeInfo().authors() != null && !item.volumeInfo().authors().isEmpty() ? String.join(", ", item.volumeInfo().authors()) : "Autor desconhecido";
        String isbn = item.volumeInfo().industryIdentifiers() != null && !item.volumeInfo().industryIdentifiers().isEmpty() ? item.volumeInfo().industryIdentifiers().get(0).identifier() : "ISBN desconhecido";
        LocalDate dataPublicacao = formatarData(item.volumeInfo().publishedDate());
        String categoria = item.volumeInfo().categories() != null && !item.volumeInfo().categories().isEmpty() ? String.join(", ", item.volumeInfo().categories()) : "Categoria desconhecida";

        return new DadosLivroCadastro(null, titulo, autor, isbn, dataPublicacao, categoria
        );
    }

    private LocalDate formatarData(String data) {
        if (data == null || data.isBlank()) {
            return LocalDate.now();
        }
        try {
            if (data.length() == 4) {
                return LocalDate.parse(data + "-01-01");
            } else if (data.length() == 7) {
                return LocalDate.parse(data + "-01");
            } else {
                return LocalDate.parse(data);
            }
        } catch (Exception e) {
            return LocalDate.now();
        }
    }
}
