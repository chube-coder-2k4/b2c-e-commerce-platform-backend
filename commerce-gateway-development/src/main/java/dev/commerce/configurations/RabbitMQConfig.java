package dev.commerce.configurations;

public class RabbitMQConfig {
    // RabbitMQConfig ở đây có tác dụng cấu hình các thành phần liên quan đến RabbitMQ trong ứng dụng Spring Boot.
    // Nó định nghĩa các bean cần thiết để kết nối và tương tác với RabbitMQ, bao gồm ConnectionFactory, RabbitTemplate, và các cấu hình khác như queues, exchanges, và bindings.
    // Điều này giúp ứng dụng có thể gửi và nhận tin nhắn thông qua RabbitMQ một cách hiệu quả.
    // Rabbit trong hệ thống sẽ làm những việc như gửi thông báo, xử lý các tác vụ bất đồng bộ, và quản lý hàng đợi tin nhắn.
    // Ví dụ trong project commerce này có thể sử dụng RabbitMQ để gửi thông báo về các sự kiện như tạo đơn hàng mới, cập nhật trạng thái đơn hàng, hoặc xử lý các tác vụ liên quan đến giỏ hàng một cách hiệu quả và không làm gián đoạn trải nghiệm người dùng.
}
