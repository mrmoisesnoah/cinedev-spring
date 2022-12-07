package br.com.dbc.vemser.cinedev.repository;

import br.com.dbc.vemser.cinedev.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    Optional<UsuarioEntity> findByEmailAndSenha(String email, String senha);
    Optional<UsuarioEntity> findByEmail(String email);
}
