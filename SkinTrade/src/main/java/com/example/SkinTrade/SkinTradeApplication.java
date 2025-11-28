package com.example.SkinTrade;

import com.example.SkinTrade.controller.AuthController; // Importa tu clase
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import; // <--- Necesario

@SpringBootApplication
@ComponentScan(basePackages = "com.example.SkinTrade")
@Import(AuthController.class) // <--- ¡ESTO ES OBLIGATORIO AHORA MISMO!
public class SkinTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkinTradeApplication.class, args);
    }

    // Diagnóstico al iniciar
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("==========================================");
            System.out.println("   VERIFICANDO CARGA DE CONTROLADORES   ");
            
            if (ctx.containsBean("authController")) {
                System.out.println("✅ ÉXITO: AuthController ESTÁ VIVO Y CARGADO.");
            } else {
                System.err.println("❌ ERROR CRÍTICO: AuthController NO APARECE.");
            }
            System.out.println("==========================================");
        };
    }
}