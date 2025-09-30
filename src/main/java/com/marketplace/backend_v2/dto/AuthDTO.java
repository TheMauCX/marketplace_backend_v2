package com.marketplace.backend_v2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public class AuthDTO {

    @Builder
    public record LoginRequest(
            @NotBlank(message = "El usuario es obligatorio")
            String username,

            @NotBlank(message = "La contraseña es obligatoria")
            String password
    ) {}

    @Builder
    public record LoginResponse(
            String token,
            String tipoToken,
            Long usuarioId,
            String username,
            String email
    ) {
        public LoginResponse(String token, Long usuarioId, String username, String email) {
            this(token, "Bearer", usuarioId, username, email);
        }
    }

    @Builder
    public record RegisterRequest(
            @NotBlank(message = "El usuario es obligatorio")
            @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
            String username,

            @NotBlank(message = "La contraseña es obligatoria")
            @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
            String password,

            @NotBlank(message = "El email es obligatorio")
            @Email(message = "El formato del email no es válido")
            String email
    ) {}
}