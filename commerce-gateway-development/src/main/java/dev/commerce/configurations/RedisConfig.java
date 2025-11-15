package dev.commerce.configurations;

public class RedisConfig {
    // class này dùng để cấu hình redis sử dụng cho cachable và set TTL cho các key trong redis
    // dự tính sẽ cấu hình những cái sau :
    // 1. jedisConnectionFactory : thiết lập kết nối đến redis server nếu không có sẽ sử dụng mặc định localhost:6379
    // nên phải cấu hình để kết nối đến redis server trên cloud
    // 2. redisConnectionFactory : thiết lập kết nối redis factory sử dụng cho redisTemplate mục đích là để kết nối đến redis server nếu không có jedisConnectionFactory được sử dụng thì sẽ sử dụng redisConnectionFactory
    // 3. redisTemplate : thiết lập redis template sử dụng để thao tác với redis server ví dụ như set, get, delete key trong redis và các thao tác khác với redis server
    // 4. objectMapper : thiết lập object mapper sử dụng để chuyển đổi object sang json và ngược lại khi lưu trữ và lấy dữ liệu từ redis server, vì redis lưu trữ dữ liệu dưới dạng chuỗi nên cần phải chuyển đổi object sang json và ngược lại

}
