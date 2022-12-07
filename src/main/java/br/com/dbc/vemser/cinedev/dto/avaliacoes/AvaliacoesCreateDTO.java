package br.com.dbc.vemser.cinedev.dto.avaliacoes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AvaliacoesCreateDTO {

    @Schema(example = "Nome Filme")
    private String nome;

    @Schema(example = "nota")
    private Double nota;

    @Schema(example = "comentario")
    private String comentario;
}