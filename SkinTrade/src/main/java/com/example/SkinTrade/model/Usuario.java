package com.example.SkinTrade.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USUARIO_SEQ", allocationSize = 1)
    private Long id;

    // Campos coincidentes con Register 2.js
    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email; // Usamos Email como usuario de login

    @Column(unique = true, nullable = false)
    private String rut;

    @Column(name = "TELEFONO") 
    private String telefono; // En React es 'numero'

    @Column(nullable = false)
    private String password; 

    private String role; // "USER" o "ADMIN"

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Orden> ordenes;
}