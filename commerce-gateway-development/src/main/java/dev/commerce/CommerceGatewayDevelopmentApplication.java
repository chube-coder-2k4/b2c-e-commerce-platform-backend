package dev.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(scanBasePackages = "dev.commerce")
@EnableScheduling
@EnableMethodSecurity // enable @PreAuthorize annotation
public class CommerceGatewayDevelopmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceGatewayDevelopmentApplication.class, args);
    }

}
