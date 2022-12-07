package br.com.dbc.vemser.cinedev.entity;

import br.com.dbc.vemser.cinedev.entity.enums.TipoLog;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import java.time.LocalDate;

@Document(collection = "log")
@Getter
@Setter
public class LogEntity {

    @Id
    private String id;

    @Column(name = "tipo")
    private TipoLog tipoLog;

    @Column(name = "nome")
    private String nome;

    private LocalDate data;

}
