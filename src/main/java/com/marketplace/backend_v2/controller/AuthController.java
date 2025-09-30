package com.marketplace.backend_v2.controller;

import com.marketplace.backend_v2.dto.ApiResponse;
import com.marketplace.backend_v2.dto.AuthDTO;
import com.marketplace.backend_v2.model.Usuario;
import com.marketplace.backend_v2.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDTO.LoginResponse>> login(
            @Valid @RequestBody AuthDTO.LoginRequest loginRequest) {
        log.info("POST /auth/login - Login para usuario: {}", loginRequest.username());
        try {
            AuthDTO.LoginResponse loginResponse = authService.login(loginRequest);
            return ResponseEntity.ok(ApiResponse.success("Login exitoso", loginResponse));
        } catch (RuntimeException e) {
            log.warn("Error en login para usuario {}: {}", loginRequest.username(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno en login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error en el login: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Usuario>> register(
            @Valid @RequestBody AuthDTO.RegisterRequest registerRequest) {
        log.info("POST /auth/register - Registrando usuario: {}", registerRequest.username());
        try {
            Usuario usuario = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Usuario registrado exitosamente", usuario));
        } catch (RuntimeException e) {
            log.warn("Error en registro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error interno en registro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error en el registro: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        log.info("POST /auth/logout - Cerrando sesión");
        try {
            // En una implementación real, buscaríamos el usuario por token y haríamos logout
            return ResponseEntity.ok(ApiResponse.success("Logout exitoso", null));
        } catch (Exception e) {
            log.error("Error en logout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error en el logout: " + e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestHeader("Authorization") String token) {
        log.info("GET /auth/validate - Validando token");
        try {
            String actualToken = extractToken(token);
            boolean isValid = authService.validateToken(actualToken);
            return ResponseEntity.ok(ApiResponse.success(isValid));
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error validando token: " + e.getMessage()));
        }
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new RuntimeException("Token no válido");
    }
}