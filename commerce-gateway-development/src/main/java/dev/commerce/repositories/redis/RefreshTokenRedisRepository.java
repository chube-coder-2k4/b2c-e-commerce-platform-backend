package dev.commerce.repositories.redis;

import dev.commerce.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("refreshTokenRedisRepository")
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    void deleteByUsersId(UUID userId);

    RefreshToken findByUsersId(UUID userId);
}
