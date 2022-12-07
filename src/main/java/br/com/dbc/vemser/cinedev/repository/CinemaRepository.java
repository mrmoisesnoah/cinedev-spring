package br.com.dbc.vemser.cinedev.repository;

import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroCinemaFilmeDTO;
import br.com.dbc.vemser.cinedev.entity.CinemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<CinemaEntity, Integer> {

    Optional<CinemaEntity> findByNome(String nome);

    Optional<CinemaEntity> findByIdUsuario(Integer idUsuario);

    @Query("select c from Cinema c where c.ativo like 'S' and c.idCinema = :idCinema")
    Optional<CinemaEntity> findById(Integer idCinema);

    @Query("select c from Cinema c where c.ativo like 'S'")
    List<CinemaEntity> findAll();

    @Modifying
    @Query("update Cinema set ativo = 'N' where idCinema = :idCinema")
    void deleteById(Integer idCinema);

    @Query(" select new br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroCinemaFilmeDTO(" +
            " c.idCinema, " +
            " c.nome, " +
            " c.estado, " +
            " i.filme.idFilme, " +
            " i.filme.nome, " +
            " i.filme.classificacaoEtaria, " +
            " i.filme.duracao, " +
            " i.idIngresso, " +
            " i.preco, " +
            " i.dataHora, " +
            " i.disponibilidade, " +
            " i.ativo " +
            ")" +
            " from Cinema c " +
            " left join c.ingresso i " +
            " where (:idCinema is null or c.idCinema = :idCinema and i.idIngresso is not null )")
    List<RelatorioCadastroCinemaFilmeDTO> listarRelatorioPersonalizado(Integer idCinema);
}