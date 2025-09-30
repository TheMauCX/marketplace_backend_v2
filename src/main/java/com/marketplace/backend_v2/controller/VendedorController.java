package com.marketplace.backend_v2.controller;

import com.marketplace.backend_v2.dto.ApiResponse;
import com.marketplace.backend_v2.dto.VendedorDTO;
import com.marketplace.backend_v2.service.VendedorService;
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
@RequestMapping("/vendedores")
@CrossOrigin(origins = "*")
public class VendedorController {

    private final VendedorService vendedorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VendedorDTO>>> getAllVendedores() {
        log.info("GET /vendedores - Obteniendo todos los vendedores");
        try {
            List<VendedorDTO> vendedores = vendedorService.findAll();
            return ResponseEntity.ok(ApiResponse.success(vendedores));
        } catch (Exception e) {
            log.error("Error al obtener vendedores: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener vendedores: " + e.getMessage()));
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<VendedorDTO>>> getVendedoresActivos() {
        log.info("GET /vendedores/activos - Obteniendo vendedores activos");
        try {
            List<VendedorDTO> vendedores = vendedorService.findByEstadoTrue();
            return ResponseEntity.ok(ApiResponse.success(vendedores));
        } catch (Exception e) {
            log.error("Error al obtener vendedores activos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener vendedores activos: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendedorDTO>> getVendedorById(@PathVariable Long id) {
        log.info("GET /vendedores/{} - Obteniendo vendedor por ID", id);
        try {
            VendedorDTO vendedor = vendedorService.findById(id);
            return ResponseEntity.ok(ApiResponse.success(vendedor));
        } catch (RuntimeException e) {
            log.warn("Vendedor no encontrado con ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener vendedor con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener vendedor: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VendedorDTO>> createVendedor(
            @Valid @RequestBody VendedorDTO vendedorDTO) {
        log.info("POST /vendedores - Creando nuevo vendedor");
        try {
            VendedorDTO vendedor = vendedorService.create(vendedorDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Vendedor creado exitosamente", vendedor));
        } catch (RuntimeException e) {
            log.warn("Error al crear vendedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al crear vendedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al crear vendedor: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VendedorDTO>> updateVendedor(
            @PathVariable Long id,
            @Valid @RequestBody VendedorDTO vendedorDTO) {
        log.info("PUT /vendedores/{} - Actualizando vendedor", id);
        try {
            VendedorDTO vendedor = vendedorService.update(id, vendedorDTO);
            return ResponseEntity.ok(ApiResponse.success("Vendedor actualizado exitosamente", vendedor));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Vendedor no encontrado para actualizar: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            log.warn("Error al actualizar vendedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al actualizar vendedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al actualizar vendedor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVendedor(@PathVariable Long id) {
        log.info("DELETE /vendedores/{} - Eliminando vendedor", id);
        try {
            vendedorService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Vendedor eliminado exitosamente", null));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Vendedor no encontrado para eliminar: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            log.warn("Error al eliminar vendedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno al eliminar vendedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al eliminar vendedor: " + e.getMessage()));
        }
    }
}