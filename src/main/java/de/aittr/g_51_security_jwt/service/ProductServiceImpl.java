package de.aittr.g_51_security_jwt.service;

import de.aittr.g_51_security_jwt.domain.Product;
import de.aittr.g_51_security_jwt.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oleg Mordkovich
 * {@code @date} 16.09.2025
 */

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Product getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
       repository.deleteById(id);
    }
}
