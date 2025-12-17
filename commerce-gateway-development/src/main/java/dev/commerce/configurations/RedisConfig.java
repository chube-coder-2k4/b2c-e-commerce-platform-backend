package dev.commerce.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableRedisRepositories
@Slf4j
@EnableCaching
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
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        template.setKeySerializer(new StringRedisSerializer()); // Sử dụng StringRedisSerializer để tuần tự hóa khóa dưới dạng chuỗi
        template.setHashKeySerializer(new StringRedisSerializer()); // Sử dụng StringRedisSerializer để tuần tự hóa khóa băm dưới dạng chuỗi
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // Sử dụng GenericJackson2JsonRedisSerializer để tuần tự hóa giá trị dưới dạng JSON
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer()); // Sử dụng GenericJackson2JsonRedisSerializer để tuần tự hóa giá trị băm dưới dạng JSON
        template.afterPropertiesSet();


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

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper());

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(java.time.Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()
                )
                ).serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                serializer
                        )
                );
    } // this method cacheConfiguration này có nhiệm vụ là cung cấp cấu hình mặc định cho caching trong Redis, bao gồm các thiết lập như thời gian hết hạn của cache, cách thức tuần tự hóa dữ liệu, v.v.

    @Bean
    public RedisCacheManager cacheManager() {
        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfiguration())
                .build();
    } // this method cacheManager này có nhiệm vụ là quản lý các cache trong ứng dụng, bao gồm việc tạo, xóa và truy xuất các cache dựa trên cấu hình được cung cấp
}
