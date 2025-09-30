package com.marketplace.backend_v2.service;

import com.marketplace.backend_v2.dto.ProductoDTO;
import com.marketplace.backend_v2.model.Producto;
import com.marketplace.backend_v2.model.Vendedor;
import com.marketplace.backend_v2.repository.ProductoRepository;
import com.marketplace.backend_v2.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final VendedorRepository vendedorRepository;

    public List<ProductoDTO> findAll() {
        log.info("Buscando todos los productos");
        return productoRepository.findAll().stream()
                .map(ProductoDTO::fromEntity)
                .toList();
    }

    public ProductoDTO findById(Long id) {
        log.info("Buscando producto con ID: {}", id);
        return productoRepository.findById(id)
                .map(ProductoDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public List<ProductoDTO> findByVendedorId(Long vendedorId) {
        log.info("Buscando productos del vendedor con ID: {}", vendedorId);
        return productoRepository.findByVendedorId(vendedorId).stream()
                .map(ProductoDTO::fromEntity)
                .toList();
    }

    public List<ProductoDTO> findByCategoria(String categoria) {
        log.info("Buscando productos por categoría: {}", categoria);
        return productoRepository.findByCategoria(categoria).stream()
                .map(ProductoDTO::fromEntity)
                .toList();
    }

    public List<ProductoDTO> findProductosActivos() {
        log.info("Buscando productos activos");
        return productoRepository.findByVendedorEstadoTrue().stream()
                .map(ProductoDTO::fromEntity)
                .toList();
    }

    public ProductoDTO create(ProductoDTO productoDTO) {
        log.info("Creando nuevo producto: {}", productoDTO.nombre());

        Vendedor vendedor = vendedorRepository.findById(productoDTO.vendedorId())
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con ID: " + productoDTO.vendedorId()));

        if (!vendedor.getEstado()) {
            throw new RuntimeException("No se pueden crear productos para vendedores inactivos");
        }

        Producto producto = Producto.builder()
                .nombre(productoDTO.nombre())
                .descripcion(productoDTO.descripcion())
                .precio(productoDTO.precio())
                .stock(productoDTO.stock())
                .vendedor(vendedor)
                .categoria(productoDTO.categoria())
                .imagenUrl(productoDTO.imagenUrl())
                .activo(true)
                .build();

        Producto savedProducto = productoRepository.save(producto);
        log.info("Producto creado exitosamente con ID: {}", savedProducto.getId());

        return ProductoDTO.fromEntity(savedProducto);
    }

    public ProductoDTO update(Long id, ProductoDTO productoDTO) {
        log.info("Actualizando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        producto.setNombre(productoDTO.nombre());
        producto.setDescripcion(productoDTO.descripcion());
        producto.setPrecio(productoDTO.precio());
        producto.setStock(productoDTO.stock());
        producto.setCategoria(productoDTO.categoria());
        producto.setImagenUrl(productoDTO.imagenUrl());

        Producto updatedProducto = productoRepository.save(producto);
        log.info("Producto actualizado exitosamente con ID: {}", updatedProducto.getId());

        return ProductoDTO.fromEntity(updatedProducto);
    }

    public void deleteById(Long id) {
        log.info("Eliminando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        producto.setActivo(false);
        productoRepository.save(producto);
        log.info("Producto marcado como inactivo con ID: {}", id);
    }

    public List<ProductoDTO> findProductosConStock() {
        log.info("Buscando productos con stock disponible");
        return productoRepository.findProductosConStock().stream()
                .map(ProductoDTO::fromEntity)
                .toList();
    }
}