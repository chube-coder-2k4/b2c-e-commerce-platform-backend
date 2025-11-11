package dev.commerce.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage extends BaseEntity{
    @Id
    private UUID id;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product productId;
    private String publicId; // Cloudinary public ID
    private boolean isPrimary = false; // Indicates if this image is the primary image
    public void ensureId() {
        if(id == null) this.id = UUID.randomUUID();
    }
}
