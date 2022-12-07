package br.com.dbc.vemser.cinedev.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Cinema")
public class CinemaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CINEMA_SEQ")
    @SequenceGenerator(name = "CINEMA_SEQ", sequenceName = "seq_id_cinema", allocationSize = 1)
    @Column(name = "id_cinema")
    private Integer idCinema;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Integer idUsuario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "ativo")
    private String ativo = "S";

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cinema")
    private Set<IngressoEntity> ingresso;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuario;

}