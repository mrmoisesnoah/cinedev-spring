package br.com.dbc.vemser.cinedev.dto.avaliacoes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AvaliacoesDTO extends AvaliacoesCreateDTO{
    @Schema(example = "1")
    private String id;
}
