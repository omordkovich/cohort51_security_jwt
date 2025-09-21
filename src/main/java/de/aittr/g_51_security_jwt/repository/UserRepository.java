package de.aittr.g_51_security_jwt.repository;

import de.aittr.g_51_security_jwt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Oleg Mordkovich
 * {@code @date} 16.09.2025
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
