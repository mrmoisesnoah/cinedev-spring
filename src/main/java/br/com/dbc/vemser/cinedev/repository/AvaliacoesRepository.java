package br.com.dbc.vemser.cinedev.repository;

import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesDTOContador;
import br.com.dbc.vemser.cinedev.entity.AvaliacoesEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacoesRepository extends MongoRepository<AvaliacoesEntity, String> {

    @Aggregation(pipeline = {
            "{'$group':{ '_id': '$nota', 'quantidade' : {'$sum': 1} }}"
    })
    List<AvaliacoesDTOContador> groupByNota();

    List<AvaliacoesEntity> findAllByNota(Double nota);

    Optional<AvaliacoesEntity> findByNome(String nome);

}
