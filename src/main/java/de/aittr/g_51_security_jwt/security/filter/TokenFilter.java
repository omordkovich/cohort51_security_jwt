package de.aittr.g_51_security_jwt.security.filter;

import de.aittr.g_51_security_jwt.security.service.TokenService;
import de.aittr.g_51_security_jwt.security.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author Oleg Mordkovich
 * {@code @date} 18.09.2025
 */

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserService userService;

    public TokenFilter(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenService.getTokenFromRequest(request, "Access-Token");

        if (accessToken != null && tokenService.validateAccessToken(accessToken)) {
            Claims claims = tokenService.getAccessClaims(accessToken);
            String username = claims.getSubject();
            UserDetails userDetails = userService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
