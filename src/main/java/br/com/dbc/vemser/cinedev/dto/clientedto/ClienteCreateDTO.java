package br.com.dbc.vemser.cinedev.dto.clientedto;

import br.com.dbc.vemser.cinedev.dto.cinemadto.UsuarioCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCreateDTO extends UsuarioCreateDTO {
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 25)
    @Schema(description = "Primeiro nome da Pessoa", example = "Moises Noah")
    private String primeiroNome;

    @NotNull
    @NotEmpty
    @Size(min = 2, max = 35)
    @Schema(description = "Segundo nome da Pessoa", example = "Silva Bispo")
    private String ultimoNome;

    @NotNull
    @NotEmpty
    @Size(min = 11, max = 11)
    @Schema(description = "Cadastro de Pessoa Física, com mínimo de 11 digitos", example = "11122233344")
    private String cpf;

    @Past
    @NotNull
    @Schema(description = "Data de Nascimento do usuário", example = "1999-02-20")
    private LocalDate dataNascimento;
}
