package dev.commerce.components;

import dev.commerce.dtos.common.TokenType;
import dev.commerce.services.UserService;
import dev.commerce.services.impl.UserDetailServiceImpl;
import dev.commerce.services.JwtService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class Prefilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("------ PREFILTER ------");

        final String authorization = request.getHeader("Authorization");
        log.info("Authorization: {}", authorization);
        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = authorization.substring(7);
        log.info("Token: {} ", token);
        final String username = jwtService.extractUsername(token, TokenType.ACCESS);
        log.info("Username: {}", username);
        log.info("Role from token: {}", jwtService.extractRole(token, TokenType.ACCESS));
        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, userDetails, TokenType.ACCESS)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(securityContext);
                log.info("Successfully authenticated user {}", username);
            } else {
                log.info("Invalid username or password");
            }
        }
        filterChain.doFilter(request, response);
    }
}
