package com.marketplace.backend_v2.controller;

import com.marketplace.backend_v2.dto.ApiResponse;
import com.marketplace.backend_v2.dto.ProductoDTO;
import com.marketplace.backend_v2.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> getAllProductos() {
        log.info("GET /productos - Obteniendo todos los productos");
        try {
            List<ProductoDTO> productos = productoService.findAll();
            return ResponseEntity.ok(ApiResponse.success(productos));
        } catch (Exception e) {
            log.error("Error al obtener productos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener productos: " + e.getMessage()));
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> getProductosActivos() {
        log.info("GET /productos/activos - Obteniendo productos activos");
        try {
            List<ProductoDTO> productos = productoService.findProductosActivos();
            return ResponseEntity.ok(ApiResponse.success(productos));
        } catch (Exception e) {
            log.error("Error al obtener productos activos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener productos activos: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> getProductoById(@PathVariable Long id) {
        log.info("GET /productos/{} - Obteniendo producto por ID", id);
        try {
            ProductoDTO producto = productoService.findById(id);
            return ResponseEntity.ok(ApiResponse.success(producto));
        } catch (RuntimeException e) {
            log.warn("Producto no encontrado con ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener producto con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener producto: " + e.getMessage()));
        }
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> getProductosByVendedor(
            @PathVariable Long vendedorId) {
        log.info("GET /productos/vendedor/{} - Obteniendo productos por vendedor", vendedorId);
        try {
            List<ProductoDTO> productos = productoService.findByVendedorId(vendedorId);
            return ResponseEntity.ok(ApiResponse.success(productos));
        } catch (Exception e) {
            log.error("Error al obtener productos del vendedor {}: {}", vendedorId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener productos del vendedor: " + e.getMessage()));
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> getProductosByCategoria(
            @PathVariable String categoria) {
        log.info("GET /productos/categoria/{} - Obteniendo productos por categoría", categoria);
        try {
            List<ProductoDTO> productos = productoService.findByCategoria(categoria);
            return ResponseEntity.ok(ApiResponse.success(productos));
        } catch (Exception e) {
            log.error("Error al obtener productos por categoría {}: {}", categoria, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener productos por categoría: " + e.getMessage()));
        }
    }

    @GetMapping("/stock")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> getProductosConStock() {
        log.info("GET /productos/stock - Obteniendo productos con stock disponible");
        try {
            List<ProductoDTO> productos = productoService.findProductosConStock();
            return ResponseEntity.ok(ApiResponse.success(productos));
        } catch (Exception e) {
            log.error("Error al obtener productos con stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener productos con stock: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoDTO>> createProducto(
            @Valid @RequestBody ProductoDTO productoDTO) {
        log.info("POST /productos - Creando nuevo producto");
        try {
            ProductoDTO producto = productoService.create(productoDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Producto creado exitosamente", producto));
        } catch (RuntimeException e) {
            log.warn("Error al crear producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al crear producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al crear producto: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> updateProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO productoDTO) {
        log.info("PUT /productos/{} - Actualizando producto", id);
        try {
            ProductoDTO producto = productoService.update(id, productoDTO);
            return ResponseEntity.ok(ApiResponse.success("Producto actualizado exitosamente", producto));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Producto no encontrado para actualizar: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            log.warn("Error al actualizar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al actualizar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al actualizar producto: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProducto(@PathVariable Long id) {
        log.info("DELETE /productos/{} - Eliminando producto", id);
        try {
            productoService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Producto eliminado exitosamente", null));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Producto no encontrado para eliminar: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            log.warn("Error al eliminar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al eliminar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al eliminar producto: " + e.getMessage()));
        }
    }
}