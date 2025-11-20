package dev.commerce.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableRedisRepositories
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        log.info("Connecting to Redis at {}:{}", redisHost, redisPort);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        return new JedisConnectionFactory(config);
    } // this method jedisConnectionFactory này có nv là tạo ra một kết nối đến máy chủ Redis sử dụng cấu host và port  cung cấp từ file cấu hình ứng dụng

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        log.info("Connected to Redis at {}:{}", redisHost, redisPort);
        log.info("Redis is connected successfully.");
        return template;
    } // this method redisTemplate này có nhiệm vụ là cung cấp một giao diện để tương tác với Redis, cho phép thực hiện các thao tác như lưu trữ, truy xuất và xóa dữ liệu trong Redis thông qua các phương thức của RedisTemplate

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        mapper.registerModule(module);
        return mapper;
    } // this method objectmapper này có nhiệm vụ là chuyển đổi các đối tượng LocalDateTime sang định dạng chuỗi ISO_DATE_TIME khi lưu trữ vào Redis và ngược lại khi lấy dữ liệu từ Redis
}
