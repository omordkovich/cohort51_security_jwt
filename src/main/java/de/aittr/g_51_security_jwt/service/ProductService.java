package de.aittr.g_51_security_jwt.service;

import de.aittr.g_51_security_jwt.domain.Product;

import java.util.List;

/**
 * @author Oleg Mordkovich
 * {@code @date} 16.09.2025
 */
public interface ProductService {

    Product save(Product product);

    List<Product> getAll();

    Product getById(Long id);

    void deleteById(Long id);
}
