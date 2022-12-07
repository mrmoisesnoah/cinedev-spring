package br.com.dbc.vemser.cinedev.dto.ingressodto;

import br.com.dbc.vemser.cinedev.entity.enums.Disponibilidade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngressoCompradoDTO {

    private Integer idIngresso;
    private Integer idFilme;
    private Integer idCinema;
    private Integer idCliente;
    @JsonIgnore
    private String cpf;
    private String nomeCliente;
    private String nomeFilme;
    private String nomeCinema;
    private LocalDateTime dataHora;
    private Disponibilidade disponibilidade;
    private Double preco;
    private String ativo;

}
