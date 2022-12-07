package br.com.dbc.vemser.cinedev.dto.filmedto;

import br.com.dbc.vemser.cinedev.entity.enums.Idioma;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmeCreateDTO {

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 200)
    @Schema(description = "Título do filme que deseja cadastrar!", example = "STAR WARS")
    private String nome;

    @NotNull
    @Schema(description = "Linguagens disponíveis para o espectador.", example = "DUBLADO")
    private Idioma idioma;

    @NotNull
    @Schema(description = "Sensura, ou limite da faixa etaria permitido pelo filme.", example = "13")
    private Integer classificacaoEtaria;

    @NotNull
    @Schema(description = "Duração do filme em minutos.", example = "120")
    private Integer duracao;
}
