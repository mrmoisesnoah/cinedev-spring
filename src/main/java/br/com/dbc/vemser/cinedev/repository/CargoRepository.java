package br.com.dbc.vemser.cinedev.repository;

import br.com.dbc.vemser.cinedev.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository<CargoEntity, Integer> {
}
