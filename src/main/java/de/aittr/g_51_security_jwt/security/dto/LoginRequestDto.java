package de.aittr.g_51_security_jwt.security.dto;

import java.util.Objects;

/**
 * @author Oleg Mordkovich
 * {@code @date} 18.09.2025
 */
public class LoginRequestDto {
    private String username;
    private String password;

    public LoginRequestDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LoginRequestDto that)) return false;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return String.format("username: %s, password: %s", username, password);
    }
}
