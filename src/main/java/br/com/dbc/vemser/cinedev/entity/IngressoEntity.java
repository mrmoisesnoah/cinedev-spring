package br.com.dbc.vemser.cinedev.entity;

import br.com.dbc.vemser.cinedev.entity.enums.Disponibilidade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "Ingresso")
public class IngressoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INGRESSO")
    @SequenceGenerator(name = "SEQ_INGRESSO", sequenceName = "seq_id_ingresso", allocationSize = 1)
    @Column(name = "id_ingresso")
    private Integer idIngresso;

    @Column(name = "id_filme", insertable = false, updatable = false)
    private Integer idFilme;

    @Column(name = "id_cinema", insertable = false, updatable = false)
    private Integer idCinema;

    @Column(name = "id_cliente", insertable = false, updatable = false)
    private Integer idCliente;

    @Column(name = "valor")
    private Double preco;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "disponibilidade")
    @Enumerated(EnumType.STRING)
    private Disponibilidade disponibilidade;
    @Column(name = "ativo")
    private String ativo = "S";

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cinema", referencedColumnName = "id_cinema")
    @ToString.Exclude
    private CinemaEntity cinema;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    @ToString.Exclude
    private ClienteEntity cliente;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_filme", referencedColumnName = "id_filme")
    @ToString.Exclude
    private FilmeEntity filme;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IngressoEntity that = (IngressoEntity) o;
        return idIngresso != null && Objects.equals(idIngresso, that.idIngresso);
    }

}
