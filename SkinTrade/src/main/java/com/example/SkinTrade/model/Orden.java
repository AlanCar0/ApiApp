package com.example.SkinTrade.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ORDEN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orden_seq")
    @SequenceGenerator(name = "orden_seq", sequenceName = "ORDEN_SEQ", allocationSize = 1)
    private Long id;

    private LocalDateTime fechaCompra;
    private double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL)
    private List<DetalleOrden> detalles;
    
    @PrePersist
    protected void onCreate() {
        fechaCompra = LocalDateTime.now();
    }
}