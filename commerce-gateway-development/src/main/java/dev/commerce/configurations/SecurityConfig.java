package dev.commerce.configurations;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**") // mapping ở đây để cho phép tất cả các endpoint ví dụ như /api/** , /auth/** , ...
                        .allowedOrigins("*") // mapping ở đây để cho phép tất cả các domain có thể truy cập vào api ví dụ như http://localhost:3000 , http://example.com , ...
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // cho phép tất cả các header ví dụ như Authorization , Content-Type , ...
                        .allowCredentials(false) // cho phép gửi cookie hoặc không nhưng ở đây để false vì allowedOrigins là * nên không thể gửi cookie nếu là true thì phải chỉ định rõ domain
                        .maxAge(3600); // maxage ở đây là thời gian mà trình duyệt sẽ cache các thiết lập CORS này trong bao lâu (tính bằng giây) ví dụ hết 3600 giây thì trình duyệt sẽ gửi lại yêu cầu preflight
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
