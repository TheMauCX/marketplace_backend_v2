package com.marketplace.backend_v2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductoDTO(
        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        Integer stock,

        @NotNull(message = "El ID del vendedor es obligatorio")
        Long vendedorId,

        @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
        String categoria,

        String imagenUrl,

        LocalDateTime fechaCreacion,

        // Información del vendedor (solo para respuestas)
        String vendedorNombre,
        String vendedorEmail
) {
    public ProductoDTO {
        if (stock == null) stock = 0;
    }

    public static ProductoDTO fromEntity(com.marketplace.backend_v2.model.Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .vendedorId(producto.getVendedor().getId())
                .categoria(producto.getCategoria())
                .imagenUrl(producto.getImagenUrl())
                .fechaCreacion(producto.getFechaCreacion())
                .vendedorNombre(producto.getVendedor().getNombre())
                .vendedorEmail(producto.getVendedor().getEmail())
                .build();
    }
}
