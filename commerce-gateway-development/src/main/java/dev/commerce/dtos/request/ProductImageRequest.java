package dev.commerce.dtos.request;

import jakarta.mail.Multipart;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageRequest {
    private boolean isPrimary;
    private MultipartFile file;

}
