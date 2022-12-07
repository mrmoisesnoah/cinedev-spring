package br.com.dbc.vemser.cinedev.repository;

import br.com.dbc.vemser.cinedev.entity.IngressoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngressoRepository extends JpaRepository<IngressoEntity, Integer> {


    @Query("SELECT i FROM Ingresso i WHERE i.disponibilidade = 'N' and i.ativo = 'S'")
    List<IngressoEntity> findIngressoComprados();

    @Query("SELECT i FROM Ingresso i WHERE i.disponibilidade = 'S' and i.ativo = 'S'")
    List<IngressoEntity> findIngressoDisponiveis();

    @Query("select i from Ingresso i where i.ativo = 'S' and i.idIngresso = :idIngresso")
    Optional<IngressoEntity> findById(Integer idIngresso);

    @Query("select i from Ingresso i where i.ativo = 'S'")
    List<IngressoEntity> findAll();

    @Modifying
    @Query("update Ingresso set ativo = 'N' where idIngresso = :idIngresso")
    void deleteById(Integer idIngresso);

}