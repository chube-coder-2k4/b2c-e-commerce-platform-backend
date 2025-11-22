package dev.commerce.repositories.redis;

import dev.commerce.redis.OtpVerify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository("otpVerifyRedisRepository")
public interface OtpVerifyRepository extends CrudRepository<OtpVerify, String> {
}
