package br.com.dbc.vemser.cinedev.dto.relatorios;

import br.com.dbc.vemser.cinedev.entity.enums.Disponibilidade;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class RelatorioCadastroCinemaFilmeDTO {

    private Integer idCinema; // cinema
    private String nome; // cinema
    private String estado; // cinema
    private Integer idFilme; // filme
    private String nomeFilme; // filme
    private Integer classificacaoEtaria; // filme
    private Integer duracao; // filme
    private Integer idIngresso; // ingresso
    private Double preco; // ingresso
    private LocalDateTime dataHora; // ingresso
    private Disponibilidade disponibilidade; // ingresso
    private String ativo;
}

//Cinemas cadastrados, seus filmes e ingressos