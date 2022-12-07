package br.com.dbc.vemser.cinedev.dto.recuperarsenhadto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RecuperarSenhaDTO {

        @NotNull
        private String email;

}
