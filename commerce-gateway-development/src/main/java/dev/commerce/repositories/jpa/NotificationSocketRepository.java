//package dev.commerce.repositories.jpa;
//
//import dev.commerce.entitys.NotificationSocket;
//import dev.commerce.entitys.Users;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.UUID;
//
//public interface NotificationSocketRepository extends JpaRepository<NotificationSocket, UUID> {
//
//    List<NotificationSocket> findByUserOrderByCreatedAtDesc(Users user);
//}
