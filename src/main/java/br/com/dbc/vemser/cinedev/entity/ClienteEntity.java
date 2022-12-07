package br.com.dbc.vemser.cinedev.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Cliente")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENTE_SEQ")
    @SequenceGenerator(name = "CLIENTE_SEQ", sequenceName = "seq_id_cliente", allocationSize = 1)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Integer idUsuario;

    @Column(name = "primeiro_nome")
    private String primeiroNome;

    @Column(name = "ultimo_nome")
    private String ultimoNome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "ativo")
    private String ativo = "S";

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    private Set<IngressoEntity> ingresso;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuario;
}
