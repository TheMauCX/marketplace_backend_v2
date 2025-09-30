package com.marketplace.backend_v2.repository;

import com.marketplace.backend_v2.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByVendedorId(Long vendedorId);
    List<Producto> findByCategoria(String categoria);

    @Query("SELECT p FROM Producto p WHERE p.vendedor.estado = true AND p.activo = true")
    List<Producto> findByVendedorEstadoTrue();

    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.vendedor.estado = true AND p.activo = true")
    List<Producto> findByCategoriaAndVendedorActivo(@Param("categoria") String categoria);

    @Query("SELECT p FROM Producto p WHERE p.stock > 0 AND p.vendedor.estado = true AND p.activo = true")
    List<Producto> findProductosConStock();

    List<Producto> findByActivoTrue();
}
