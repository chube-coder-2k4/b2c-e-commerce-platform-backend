package dev.commerce.entitys;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity{
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private String provider; // e.g., "PayPal", "Stripe"
    private double amount;
    private String transactionId;
    private String status; // e.g., "COMPLETED", "PENDING", "
    // set paidAt when status is "COMPLETED"
    private LocalDateTime paidAt;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
