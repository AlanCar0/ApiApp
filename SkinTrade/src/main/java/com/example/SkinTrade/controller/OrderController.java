package com.example.SkinTrade.controller;

import com.example.SkinTrade.model.*;
import com.example.SkinTrade.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final OrdenRepository ordenRepo; // Necesitas crear este Repo (ver abajo)

    // Constructor con inyección de dependencias
    public OrderController(UserRepository userRepo, ProductRepository productRepo, OrdenRepository ordenRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.ordenRepo = ordenRepo;
    }

    @PostMapping("/checkout")
    @Transactional
    public ResponseEntity<?> checkout(@RequestBody CompraSimple request) {
        // 1. Buscar Usuario
        Usuario usuario = userRepo.findById(request.usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Crear la Orden (Cabecera)
        Orden orden = new Orden();
        orden.setUsuario(usuario);
        orden.setDetalles(new ArrayList<>());
        
        double totalCalculado = 0;

        // 3. Procesar productos del carrito
        for (ItemSimple item : request.items) {
            Product producto = productRepo.findById(item.productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no existe ID: " + item.productoId));

            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(orden);
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());

            orden.getDetalles().add(detalle);
            totalCalculado += (producto.getPrecio() * item.cantidad);
        }

        orden.setTotal(totalCalculado);
        ordenRepo.save(orden);

        return ResponseEntity.ok(Map.of("status", "success", "ordenId", orden.getId()));
    }

    // --- CLASES INTERNAS (Para no crear archivos DTO aparte) ---
    // Esto reemplaza a la carpeta 'dto' y mantiene todo junto
    public static class CompraSimple {
        public Long usuarioId;
        public List<ItemSimple> items;
    }

    public static class ItemSimple {
        public Long productoId;
        public int cantidad;
    }
}