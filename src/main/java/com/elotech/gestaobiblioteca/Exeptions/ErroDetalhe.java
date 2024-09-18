package com.elotech.gestaobiblioteca.Exeptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Detalhes do erro ocorrido")
public class ErroDetalhe {

    @Schema(description = "Data e hora em que o erro ocorreu", example = "2023-04-05T14:30:00")
    private LocalDateTime dataHora;

    @Schema(description = "Código de status HTTP", example = "404")
    private int codigoStatus;

    @Schema(description = "Texto do status HTTP", example = "Not Found")
    private String textoStatus;

    @Schema(description = "Mensagem detalhada do erro", example = "Usuário não encontrado")
    private String mensagem;

    @Schema(description = "Detalhes adicionais sobre o erro")
    private List<String> detalhes;
}
