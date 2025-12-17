//package dev.commerce.entitys;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class NotificationSocket extends BaseEntity {
//    @Id
//    private UUID id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private Users user;
//
//    @Column(nullable = false)
//    private String title;
//
//    @Column(nullable = false, length = 500)
//    private String message;
//
//    @Column(nullable = false)
//    private boolean read = false;
//
//    @Column(name = "reference_id")
//    private String referenceId; // orderId
//
//    @Column(nullable = false)
//    private String type; // ORDER_STATUS, PAYMENT, SYSTEM
//
//}
