package dev.commerce.repositories;

import dev.commerce.entitys.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID>, JpaSpecificationExecutor<Users>{
    // findByEmail
    Users findByEmail(String email);
}
