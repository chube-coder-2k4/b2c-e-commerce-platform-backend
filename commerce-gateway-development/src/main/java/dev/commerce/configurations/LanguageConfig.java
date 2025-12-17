package dev.commerce.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LanguageConfig extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
// ở class này extends AcceptHeaderLocaleResolver để cấu hình ngôn ngữ dựa trên header Accept-Language trong request
// còn WebMvcConfigurer để tùy chỉnh cấu hình MVC của Spring
}
