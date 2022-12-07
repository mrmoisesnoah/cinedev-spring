package br.com.dbc.vemser.cinedev.dto.cinemadto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CinemaDTO {
    @Schema(example = "1")
    private Integer idCinema;

    @Schema(example = "CINEMARK Pier-21")
    private String nome;

    @Schema(example = "Maranhão")
    private String estado;

    @Schema(example = "Ceilândia")
    private String cidade;

    @Schema(example = "noahbispo@yahoo.com.br")
    private String email;
}
