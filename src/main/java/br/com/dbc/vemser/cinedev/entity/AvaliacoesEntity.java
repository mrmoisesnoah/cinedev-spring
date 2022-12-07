package br.com.dbc.vemser.cinedev.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "avaliacoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacoesEntity {

    @Id
    private String id;

//    @Column(name = "Nome Filme")
    private String nome;

//    @Column(name = "nota")
    private Double nota;

//    @Column(name = "comentario")
    private String comentario;

    private Integer quantidade;

}
