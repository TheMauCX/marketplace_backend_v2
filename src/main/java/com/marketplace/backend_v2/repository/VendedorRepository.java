package com.marketplace.backend_v2.repository;

import com.marketplace.backend_v2.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    Optional<Vendedor> findByEmail(String email);
    List<Vendedor> findByEstadoTrue();
    Boolean existsByEmail(String email);
}
