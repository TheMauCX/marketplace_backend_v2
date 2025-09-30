package com.marketplace.backend_v2.repository;

import com.marketplace.backend_v2.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByToken(String token);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}