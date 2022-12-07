package br.com.dbc.vemser.cinedev.dto.clientedto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    @Schema(description = "Id do cliente", example = "1")
    private Integer idCliente;

    @Schema(description = "Primeiro nome da Pessoa", example = "Moises Noah")
    private String primeiroNome;

    @Schema(description = "Segundo nome da Pessoa", example = "Silva Bispo")
    private String ultimoNome;

    @Schema(description = "Cadastro de Pessoa Física, com mínimo de 11 digitos", example = "11122233344")
    private String cpf;

    @Schema(description = "Data de Nascimento do usuário", example = "1999/02/20")
    private LocalDate dataNascimento;

    @Schema(example = "noahbispo@yahoo.com.br")
    private String email;
}
