package com.example.SkinTrade.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DETALLE_ORDEN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_seq")
    @SequenceGenerator(name = "detalle_seq", sequenceName = "DETALLE_SEQ", allocationSize = 1)
    private Long id;

    private int cantidad;
    private double precioUnitario; // Precio congelado al momento de compra

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Product producto;
}