package dev.commerce.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Locale;

@Component
@Order(1)
@Slf4j
public class LocaleDebugFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("========== LOCALE DEBUG ==========");
        String acceptLanguage = request.getHeader("Accept-Language");
        log.info("Accept-Language Header: {}", acceptLanguage);
        Locale requestLocale = request.getLocale();
        log.info("Request Locale (Servlet): {}", requestLocale);
        Locale contextLocale = LocaleContextHolder.getLocale();
        log.info("LocaleContextHolder Locale: {}", contextLocale);
        log.info("  - Language: {}", contextLocale.getLanguage());
        log.info("  - Country: {}", contextLocale.getCountry());
        log.info("  - Display Name: {}", contextLocale.getDisplayName());
        log.info("==================================");
        filterChain.doFilter(request, response);
    }
}

