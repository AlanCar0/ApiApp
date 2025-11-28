package com.example.SkinTrade.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PRODUCTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prod_seq_gen")
    @SequenceGenerator(name = "prod_seq_gen", sequenceName = "PRODUCTO_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private double precio; // Cambiado a double para coincidir con Kotlin

    private String imagen;
    
    @Column(length = 1000)
    private String description;
    
    //"skin", "agent", "case", "soundtrack"
    @Column(name = "product_type", nullable = false)
    private String productType;

    // Campos específicos
    private String category;       // Skins y Agents
    private String condition;      // Solo Skins
    private String author;         // Solo Soundtracks
    
    @Column(name = "featured_content")
    private String featuredContent; // Solo Cases
}