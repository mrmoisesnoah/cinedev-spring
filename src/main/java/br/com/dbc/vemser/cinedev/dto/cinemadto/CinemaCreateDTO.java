package br.com.dbc.vemser.cinedev.dto.cinemadto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CinemaCreateDTO extends UsuarioCreateDTO{
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 350)
    @Schema(description = "Nome Fantasia, ou Razão Social da Empresa", example = "CINEMARK Pier-21")
    private String nome;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 250)
    @Schema(description = "Estado onde se localiza o endereço a ser cadastrado.", example = "Maranhão")
    private String estado;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 350)
    @Schema(description = "Cidade onde se localiza o endereço a ser cadastrado.", example = "Ceilândia")
    private String cidade;
}