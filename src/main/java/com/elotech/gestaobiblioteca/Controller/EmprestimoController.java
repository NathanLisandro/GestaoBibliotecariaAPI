package com.elotech.gestaobiblioteca.Controller;

import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoEmprestimo;
import com.elotech.gestaobiblioteca.DTO.DadosCadastroEmprestimo;
import com.elotech.gestaobiblioteca.Service.EmprestimoService;
import com.elotech.gestaobiblioteca.model.Emprestimos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@Tag(name = "Empréstimos", description = "Gerenciamento de empréstimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    @Operation(summary = "Listar Empréstimos", description = "Retorna todos os empréstimos cadastrados com paginação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Emprestimos.class))))
    })
    public ResponseEntity<Page<Emprestimos>> listar(@RequestParam int page, @RequestParam int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(emprestimoService.listarEmprestimos(pageRequest));
    }

    @PostMapping
    @Operation(summary = "Criar Empréstimo", description = "Cria um novo empréstimo a partir dos dados fornecidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empréstimo criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Emprestimos.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação de parâmetros",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro400"))),
            @ApiResponse(responseCode = "404", description = "Usuário ou Livro não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404")))
    })
    public ResponseEntity<Emprestimos> criarEmprestimo(@RequestBody @Valid DadosCadastroEmprestimo emprestimoDto) {
        Emprestimos emprestimo = emprestimoService.criarEmprestimo(emprestimoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Empréstimo", description = "Atualiza um empréstimo existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empréstimo atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Emprestimos.class))),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404"))),
            @ApiResponse(responseCode = "400", description = "Erro de validação de parâmetros",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro400")))
    })
    public ResponseEntity<Emprestimos> atualizarEmprestimo(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoEmprestimo emprestimoDto) {
        Emprestimos emprestimo = emprestimoService.atualizarEmprestimo(id, emprestimoDto);
        return ResponseEntity.ok(emprestimo);
    }


@GetMapping("/usuario/{id}")
@Operation(summary = "Retorna uma lista de usuário", description = "Retorna todos os empréstimos relacionado ao usuário")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de empréstimos retornada com sucesso",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Emprestimos.class)))),
        @ApiResponse(responseCode = "400", description = "Erro de validação de parâmetros",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(ref = "#/components/schemas/Erro400")))
})
public List<Emprestimos> listarEmprestimos(@PathVariable Long id) {
        return emprestimoService.listarEmprestimosPeloIdUsuario(id);
}


    @GetMapping("/livro/listarEmprestimo/{id}")
    @Operation(summary = "Retorna uma lista de empréstimos", description = "Retorna todos os empréstimos relacionado ao livro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Emprestimos.class)))),
            @ApiResponse(responseCode = "400", description = "Erro de validação de parâmetros",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro400")))
    })
    public List<Emprestimos> listarEmprestimosPeloIdLivro(@PathVariable Long id){
        return emprestimoService.listarEmprestimosPeloIdLivro(id);
    }

    @GetMapping("/livro/verificarEmprestimo/{id}")
    @Operation(summary = "Retorna true ou falso", description = "Verifica se o livro possui empréstimos (seja ele ativo ou inativo)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "true ou false",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Emprestimos.class)))),
            @ApiResponse(responseCode = "400", description = "Erro de validação de parâmetros",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro400")))
    })
    public boolean verificarEmprestimoLivro(@PathVariable Long id){
        return emprestimoService.verificarEmprestimoLivro(id);
    }
}