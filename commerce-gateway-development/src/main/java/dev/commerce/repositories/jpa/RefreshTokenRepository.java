package dev.commerce.repositories.jpa;

import dev.commerce.entitys.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("refreshTokenRepository")
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID>, JpaSpecificationExecutor<RefreshToken> {
    void deleteByUsersId(UUID userId);

    RefreshToken findByUsersId(UUID userId);
}
