package br.com.dbc.vemser.cinedev.dto.ingressodto;

import br.com.dbc.vemser.cinedev.entity.enums.Disponibilidade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
public class IngressoCreateDTO {

    @Schema(description = "Id do filme", example = "1")
    private Integer idFilme;

    @Schema(description = "Id do cinema", example = "1")
    private Integer idCinema;

    @Schema(description = "Valor do ingresso ", example = "30.00")
    private Double preco;

    @Schema(description = "Data e Hora ", example = "31/10/2022 21:30")
    private LocalDateTime dataHora;

    @Schema(description = "Disponibilidade", example = "S")
    @Enumerated(EnumType.STRING)
    private Disponibilidade disponibilidade;

    @Schema(description = "Ativo", example = "S")
    private String ativo;
}
