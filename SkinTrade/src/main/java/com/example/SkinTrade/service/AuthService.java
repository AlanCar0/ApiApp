package com.example.SkinTrade.service;

import com.example.SkinTrade.model.Usuario;
import com.example.SkinTrade.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTRO (Desde Android)
    public Usuario register(String nombre, String email, String rut, String telefono, String password) {
        // Validaciones básicas para que Android no explote
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (userRepository.existsByRut(rut)) {
            throw new RuntimeException("El RUT ya está registrado");
        }

        Usuario user = new Usuario();
        user.setNombre(nombre);
        user.setEmail(email);
        user.setRut(rut);
        user.setTelefono(telefono);
        user.setPassword(passwordEncoder.encode(password)); // Texto plano gracias a SecurityConfig
        user.setRole("USER"); // Por defecto, quien se registra en la App es usuario normal

        return userRepository.save(user);
    }

    // LOGIN (Para Android)
    public Usuario login(String email, String rawPassword) {
        Optional<Usuario> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            // Compara texto plano vs texto plano
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}