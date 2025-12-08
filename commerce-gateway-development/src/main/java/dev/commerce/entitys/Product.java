package dev.commerce.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity{
    @Id
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private double price;
    private int stockQuantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    private boolean active = true;

    @PrePersist
    public void ensureId() {
        if(id == null) this.id = UUID.randomUUID();
    }
}
