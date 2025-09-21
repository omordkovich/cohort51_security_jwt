package de.aittr.g_51_security_jwt.controller;

import de.aittr.g_51_security_jwt.domain.Product;
import de.aittr.g_51_security_jwt.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Oleg Mordkovich
 * {@code @date} 16.09.2025
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    //доступ админу
    public Product save(@RequestBody Product product) {
        return service.save(product);
    }

    // доступ всем
    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    //доступ юзеру
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return service.getById(id);
    }

    //доступ админу
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
