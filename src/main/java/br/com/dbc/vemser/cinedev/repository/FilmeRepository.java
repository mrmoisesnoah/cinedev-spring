package br.com.dbc.vemser.cinedev.repository;

import br.com.dbc.vemser.cinedev.entity.FilmeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmeRepository extends JpaRepository<FilmeEntity, Integer> {

    Optional<FilmeEntity> findByNome(String filmeNome);

    @Query("select f from Filme f where f.ativo like 'S' and f.idFilme = :idFilme")
    Optional<FilmeEntity> findById(Integer idFilme);

    @Query("select f from Filme f where f.ativo like 'S'")
    List<FilmeEntity> findAll();

    @Modifying
    @Query("update Filme set ativo = 'N' where idFilme = :idFilme")
    void deleteById(Integer idFilme);
}
