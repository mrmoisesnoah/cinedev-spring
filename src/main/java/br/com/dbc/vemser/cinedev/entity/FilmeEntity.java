package br.com.dbc.vemser.cinedev.entity;

import br.com.dbc.vemser.cinedev.entity.enums.Idioma;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Filme")
public class FilmeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FILME")
    @SequenceGenerator(name = "SEQ_FILME", sequenceName = "seq_id_filme", allocationSize = 1)
    @Column(name = "id_filme")
    private Integer idFilme;

    @Column(name = "nome")
    private String nome;

    @Column(name = "idioma")
    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    @Column(name = "classificacao")
    private Integer classificacaoEtaria;

    @Column(name = "duracao")
    private Integer duracao;

    @Column(name = "ativo")
    private String ativo = "S";

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "filme")
    private Set<IngressoEntity> ingresso;


}
