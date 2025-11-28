package com.example.SkinTrade.controller;

import com.example.SkinTrade.model.Product;
import com.example.SkinTrade.service.FileStorageService;
import com.example.SkinTrade.service.ProductService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;
    private final FileStorageService fileStorage;

    public ProductController(ProductService productService, FileStorageService fileStorage) {
        this.productService = productService;
        this.fileStorage = fileStorage;
    }

    // 1. Listar todos
    @GetMapping
    public ResponseEntity<List<Product>> listAll() {
        return ResponseEntity.ok(productService.listAll());
    }

    // 2. Obtener uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    // 3. Crear Producto (Soporta Skin, Agent, Case, Soundtrack)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> create(
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") double precio,
            @RequestParam("description") String description,
            @RequestParam("productType") String productType, // skin, agent, case, soundtrack
            
            // Parámetros opcionales (Android enviará null en los que no correspondan)
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "condition", required = false) String condition,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "featuredContent", required = false) String featuredContent,
            
            // La foto
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
        Product created = productService.createProduct(
            nombre, precio, description, productType, 
            category, condition, author, featuredContent, imagen
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 4. Actualizar Producto (Igual que crear, pero con ID)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> update(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") double precio,
            // Nota: description y productType podrían ser opcionales al editar, pero aquí los pedimos para simplificar
            @RequestParam(value = "description", required = false) String description, 
            @RequestParam(value = "productType", required = false) String productType,
            
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "condition", required = false) String condition,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "featuredContent", required = false) String featuredContent,
            
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
        // Nota: Deberías tener un método updateProduct en tu Service similar a createProduct
        // Si no lo tienes, puedes reutilizar lógica o crear uno nuevo.
        // Por ahora, asumimos que ProductService tiene un método updateProduct con firma similar.
        // Si solo tienes createProduct, puedes adaptar esto después.
        
        // Para que compile con lo que tienes ahora, usaré createProduct asumiendo lógica de reemplazo
        // O lo correcto es agregar updateProduct en el servicio.
        return ResponseEntity.ok().build(); // Placeholder si no implementaste update en el servicio aún.
    }

    // 5. Eliminar Producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // 6. Servir Imagen
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        Resource resource = fileStorage.loadFileAsResource(filename);
        String contentType = "application/octet-stream";
        try { 
            contentType = Files.probeContentType(resource.getFile().toPath()); 
        } catch (Exception ex) { }
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}