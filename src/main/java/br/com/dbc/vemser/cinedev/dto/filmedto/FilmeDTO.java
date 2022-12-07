package br.com.dbc.vemser.cinedev.dto.filmedto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmeDTO extends FilmeCreateDTO {

    @Schema(description = "Id do filme", example = "1")
    private Integer idFilme;
}
