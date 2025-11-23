package dev.commerce.repositories.jpa;

import dev.commerce.entitys.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Set<Role> findByNameIn(Set<String> name);
}
