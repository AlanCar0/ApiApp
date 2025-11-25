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

    @GetMapping
    public ResponseEntity<List<Product>> listAll() {
        return ResponseEntity.ok(productService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> create(
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") double precio,
            @RequestParam("description") String description,
            @RequestParam("productType") String productType, // skin, agent, case, etc.
            
            // Parámetros opcionales según el tipo
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "condition", required = false) String condition,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "featuredContent", required = false) String featuredContent,
            
            @RequestPart(value = "imagen", required = false) MultipartFile imagen
    ) {
        Product created = productService.createProduct(
            nombre, precio, description, productType, 
            category, condition, author, featuredContent, imagen
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        Resource resource = fileStorage.loadFileAsResource(filename);
        String contentType = "application/octet-stream";
        try { contentType = Files.probeContentType(resource.getFile().toPath()); } catch (Exception ex) { }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }
}