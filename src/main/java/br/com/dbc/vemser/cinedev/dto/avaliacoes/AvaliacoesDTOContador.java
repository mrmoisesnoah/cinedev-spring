package br.com.dbc.vemser.cinedev.dto.avaliacoes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
public class AvaliacoesDTOContador {
    @Schema(example = "9.5")
    private Double nota;

    @Schema(example = "23")
    private Integer quantidade;
}
