package de.aittr.g_51_security_jwt.repository;

import de.aittr.g_51_security_jwt.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Oleg Mordkovich
 * {@code @date} 16.09.2025
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
