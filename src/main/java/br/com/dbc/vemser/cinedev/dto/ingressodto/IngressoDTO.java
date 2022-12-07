package br.com.dbc.vemser.cinedev.dto.ingressodto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IngressoDTO extends IngressoCreateDTO {

    @Schema(description = "Id do ingresso", example = "1")
    private Integer idIngresso;

    @Schema(description = "Id do cliente", example = "1")
    private Integer idCliente;

}
