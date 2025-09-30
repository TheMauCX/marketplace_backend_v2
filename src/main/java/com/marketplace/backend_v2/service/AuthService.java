package com.marketplace.backend_v2.service;

import com.marketplace.backend_v2.dto.AuthDTO;
import com.marketplace.backend_v2.model.Usuario;
import com.marketplace.backend_v2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthDTO.LoginResponse login(AuthDTO.LoginRequest loginRequest) {
        log.info("Intentando login para usuario: {}", loginRequest.username());

        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(loginRequest.username());

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(loginRequest.password(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        String token = generateToken();
        usuario.setToken(token);
        usuario.setUltimoLogin(LocalDateTime.now());

        usuarioRepository.save(usuario);
        log.info("Login exitoso para usuario: {}", loginRequest.username());

        return AuthDTO.LoginResponse.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .build();
    }

    public Usuario register(AuthDTO.RegisterRequest registerRequest) {
        log.info("Registrando nuevo usuario: {}", registerRequest.username());

        if (usuarioRepository.existsByUsername(registerRequest.username())) {
            throw new RuntimeException("El username ya está en uso");
        }

        if (usuarioRepository.existsByEmail(registerRequest.email())) {
            throw new RuntimeException("El email ya está en uso");
        }

        Usuario usuario = Usuario.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .email(registerRequest.email())
                .activo(true)
                .build();

        Usuario savedUsuario = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente con ID: {}", savedUsuario.getId());

        return savedUsuario;
    }

    public void logout(Long usuarioId) {
        log.info("Cerrando sesión para usuario ID: {}", usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setToken(null);
        usuarioRepository.save(usuario);
        log.info("Sesión cerrada exitosamente para usuario ID: {}", usuarioId);
    }

    public boolean validateToken(String token) {
        return usuarioRepository.findByToken(token).isPresent();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}