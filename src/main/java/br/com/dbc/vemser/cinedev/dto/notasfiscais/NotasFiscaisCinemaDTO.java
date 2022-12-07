package br.com.dbc.vemser.cinedev.dto.notasfiscais;

import br.com.dbc.vemser.cinedev.entity.enums.Disponibilidade;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotasFiscaisCinemaDTO {

    private String usuario;
    private String mensagem;
    private Integer idIngresso;
    private Integer idFilme;
    private Integer idCinema;
    private Integer idCliente;
    private String cpf;
    private String nomeCliente;
    private String nomeFilme;
    private String nomeCinema;
    private LocalDateTime dataHora;
    private Disponibilidade disponibilidade;
    private Double preco;
    private String ativo;
}
