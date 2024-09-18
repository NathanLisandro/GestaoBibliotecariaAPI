package com.elotech.gestaobiblioteca.Controller;

import com.elotech.gestaobiblioteca.DTO.DadosAtualizacaoUsuario;
import com.elotech.gestaobiblioteca.DTO.DadosUsuarioCadastro;
import com.elotech.gestaobiblioteca.model.Usuario;
import com.elotech.gestaobiblioteca.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Criar Usuário", description = "Cadastra um novo usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação de parâmetros",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro400"))),
            @ApiResponse(responseCode = "409", description = "Violação de integridade de dados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro409")))
    })
    public ResponseEntity<Usuario> criarUsuario(@RequestBody @Valid DadosUsuarioCadastro usuario) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping
    @Operation(summary = "Atualizar Usuário", description = "Atualiza as informações de um usuário existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404")))
    })
    public ResponseEntity<Usuario> atualizarUsuario(@RequestBody @Valid DadosAtualizacaoUsuario usuarioDto) {
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(usuarioDto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Usuário por ID", description = "Retorna um usuário pelo ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404")))
    })
    public ResponseEntity<Usuario> buscarUsuarioPeloId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarUsuarioPeloId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @Operation(summary = "Listar Usuários", description = "Retorna uma lista de todos os usuários cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)))
    })
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Usuário", description = "Remove um usuário pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404")))
    })
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/Erro404")))
    })
    @DeleteMapping("/{id}/excluirComEmprestimos")
    public ResponseEntity<Void> excluirUsuarioComEmprestimos(@PathVariable Long id) {
        usuarioService.excluirUsuarioComEmprestimos(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
