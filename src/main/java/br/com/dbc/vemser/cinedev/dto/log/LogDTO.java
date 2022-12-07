package br.com.dbc.vemser.cinedev.dto.log;

import br.com.dbc.vemser.cinedev.entity.enums.TipoLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {
    @Schema(example = "1")
    private String id;

    @Schema(example = "Exemplo LogNome")
    private String nome;

    @Schema(example = "INGRESSOS")
    private TipoLog tipoLog;

    @Schema(example = "2022-11-24")
    private LocalDate data;
}
