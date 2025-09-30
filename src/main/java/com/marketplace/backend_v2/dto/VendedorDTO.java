package com.marketplace.backend_v2.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record VendedorDTO(
        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        String email,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String telefono,

        @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
        String direccion,

        @Size(max = 20, message = "El RUC/DNI no puede exceder 20 caracteres")
        String rucDni,

        Boolean estado,

        LocalDateTime fechaCreacion
) {
    public VendedorDTO {
        if (estado == null) estado = true;
    }

    public static VendedorDTO fromEntity(com.marketplace.backend_v2.model.Vendedor vendedor) {
        return VendedorDTO.builder()
                .id(vendedor.getId())
                .nombre(vendedor.getNombre())
                .email(vendedor.getEmail())
                .telefono(vendedor.getTelefono())
                .direccion(vendedor.getDireccion())
                .rucDni(vendedor.getRucDni())
                .estado(vendedor.getEstado())
                .fechaCreacion(vendedor.getFechaCreacion())
                .build();
    }
}
