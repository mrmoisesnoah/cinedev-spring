package br.com.dbc.vemser.cinedev.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsuarioDTO {

    @NotNull
    private Integer idUsuario;

    @NotNull
    @Schema(example = "user")
    private String email;

    @JsonIgnore
    @NotNull
    @Schema(example = "123")
    private String senha;
}
