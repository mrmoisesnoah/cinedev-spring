package br.com.dbc.vemser.cinedev.dto.relatorios;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RelatorioCadastroIngressoClienteDTO {

    private Integer idCliente; // cliente
    private String primeiroNome; // cliente
    private String ultimoNome; // cliente
    private String cpf; // cliente
    private LocalDate dataNascimento; // cliente
    private String email; // cliente
    private int idIngresso; // ingresso
    private int idFilme; // ingresso
    private String nome; // filme
    private int idCinema; // ingresso
    private LocalDateTime dataHora; // ingresso
    private String nomeCinema; // cinema
    private String estado; // cinema
    private String cidade; // cinema
    private String ativo;
}

//Clientes cadastrados e ingresso comprados pelos mesmos
