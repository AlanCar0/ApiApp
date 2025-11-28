package com.example.SkinTrade.controller;

import com.example.SkinTrade.model.Usuario;
import com.example.SkinTrade.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // REGISTRO
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam("nombre") String nombre,
            @RequestParam("rut") String rut,
            @RequestParam("email") String email,
            @RequestParam("numero") String numero,
            @RequestParam("password") String password
    ) {
        try {
            System.out.println("--- INTENTO REGISTRO ---");
            System.out.println("Datos: " + nombre + ", " + email);
            
            Usuario newUser = authService.register(nombre, email, rut, numero, password);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Usuario registrado correctamente",
                "userId", newUser.getId()
            ));
        } catch (Exception e) {
            System.err.println("!!! ERROR EN REGISTRO !!!");
            e.printStackTrace(); // ESTO MOSTRARÁ EL ERROR REAL EN LA TERMINAL
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // LOGIN (Aquí es donde tienes el error 500)
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam("email") String email, 
            @RequestParam("password") String password
    ) {
        try {
            System.out.println("--- INTENTO LOGIN ---");
            System.out.println("Email recibido: " + email);
            System.out.println("Password recibido: " + password);

            // 1. Verificar si el servicio existe
            if (authService == null) {
                throw new RuntimeException("FATAL: AuthService es NULL (Error de inyección)");
            }

            // 2. Llamar al servicio
            System.out.println("Llamando a authService.login()...");
            Usuario user = authService.login(email, password);
            
            System.out.println("Resultado del servicio: " + (user != null ? "Usuario encontrado" : "NULL"));

            if (user != null) {
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "userId", user.getId(),
                    "nombre", user.getNombre(),
                    "role", user.getRole()
                ));
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas o usuario no existe"));
            }

        } catch (Exception e) {
            // 3. ATRAPAR CUALQUIER ERROR Y MOSTRARLO
            System.err.println("!!! ERROR CRÍTICO EN LOGIN !!!");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace(); // IMPRIME EL ERROR COMPLETO EN LA CONSOLA
            
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno: " + e.getMessage()));
        }
    }
}