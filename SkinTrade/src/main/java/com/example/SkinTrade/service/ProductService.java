package com.example.SkinTrade.service;

import com.example.SkinTrade.exception.ResourceNotFoundException;
import com.example.SkinTrade.model.Product;
import com.example.SkinTrade.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repo;
    private final FileStorageService fileStorage;

    public ProductService(ProductRepository repo, FileStorageService fileStorage) {
        this.repo = repo;
        this.fileStorage = fileStorage;
    }

    // LISTAR
    public List<Product> listAll() {
        return repo.findAll();
    }

    // OBTENER UNO
    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado ID: " + id));
    }

    // CREAR (Con todos los campos nuevos)
    public Product createProduct(
            String nombre, double precio, String description, 
            String productType, String category, String condition, 
            String author, String featuredContent, MultipartFile imagen
    ) {
        Product p = new Product();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setDescription(description);
        p.setProductType(productType);
        
        // Campos opcionales
        p.setCategory(category);
        p.setCondition(condition);
        p.setAuthor(author);
        p.setFeaturedContent(featuredContent);

        if (imagen != null && !imagen.isEmpty()) {
            p.setImagen(fileStorage.storeFile(imagen));
        }
        return repo.save(p);
    }

    // ELIMINAR
    public void deleteProduct(Long id) {
        Product existing = getById(id);
        if (existing.getImagen() != null) fileStorage.deleteFile(existing.getImagen());
        repo.delete(existing);
    }
}