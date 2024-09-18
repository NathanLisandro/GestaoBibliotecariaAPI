package com.elotech.gestaobiblioteca.Controller;

import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoLivro;
import com.elotech.gestaobiblioteca.DTO.DadosLivroCadastro;
import com.elotech.gestaobiblioteca.model.Livros;
import com.elotech.gestaobiblioteca.Service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Gerenciamento de livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    @Operation(summary = "Listar Livros", description = "Retorna uma lista de todos os livros cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Livros.class)))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro500")))
    })
    public ResponseEntity<List<Livros>> listarLivros() {
        return ResponseEntity.ok(livroService.listarLivros());
    }

    @PostMapping
    @Operation(summary = "Cadastrar Livro", description = "Cadastra um novo livro.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livros.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação de parâmetros",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro400"))),
            @ApiResponse(responseCode = "409", description = "Violação de integridade de dados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro409"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro500")))
    })
    public ResponseEntity<Livros> cadastrarLivro(
            @RequestBody @Valid DadosLivroCadastro livroDto) {
        Livros livro = livroService.cadastrarLivro(livroDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(livro);
    }

    @PutMapping
    @Operation(summary = "Atualizar Livro", description = "Atualiza as informações de um livro existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livros.class))),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404"))),
            @ApiResponse(responseCode = "409", description = "Violação de integridade de dados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro409"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro500")))
    })
    public ResponseEntity<Livros> atualizarLivro(
            @RequestBody @Valid DadosAtualizacaoLivro livroDto) {
        Livros livro = livroService.atualizarLivros(livroDto);
        return ResponseEntity.ok(livro);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Livro", description = "Remove um livro pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro500")))
    })
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @Operation(summary = "Buscar livro pelo ID", description = "Busca livro pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro retornado com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Livros.class)))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro500")))
    })
    @GetMapping("/livro/{id}")
    public ResponseEntity<Livros> buscarLivro(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarLivro(id));
    }

    @Operation(summary = "Deletar Livro", description = "Remove um livro pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro500")))
    })
    @DeleteMapping("/{id}/excluirComEmprestimos")
    public ResponseEntity<Void> excluirLivroComEmprestimos(@PathVariable Long id) {
        livroService.deletarLivroComEmprestimos(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/recomendacoes/{usuarioId}")
    @Operation(summary = "Recomendar Livros", description = "Recomenda livros com base nos empréstimos anteriores do usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livros recomendados com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Livros.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404")))
    })
    public ResponseEntity<List<Livros>> recomendarLivros(@PathVariable Long usuarioId) {
        List<Livros> recomendacoes = livroService.recomendarLivros(usuarioId);
        return ResponseEntity.ok(recomendacoes);
    }
}
