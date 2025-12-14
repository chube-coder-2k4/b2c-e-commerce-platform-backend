package dev.commerce.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {


    // class này sẽ chứa các cấu hình liên quan đến WebSocket trong ứng dụng Spring Boot
    // mục đích là để thiết lập các endpoint WebSocket, cấu hình message broker, và các thiết lập liên quan khác
    // ta sẽ sử dụng @EnableWebSocketMessageBroker để kích hoạt hỗ trợ WebSocket message handling,
    // và triển khai WebSocketMessageBrokerConfigurer để tùy chỉnh cấu hình

    @Override
    public void registerStompEndpoints(org.springframework.web.socket.config.annotation.StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Đăng ký endpoint WebSocket tại đường dẫn /ws -> client sẽ kết nối đến endpoint này ví dụ localhost:8080/ws
                .setAllowedOriginPatterns("*") // Cho phép tất cả các nguồn gốc (origins) kết nối ví dụ localhost:3000 cho frontend
                .withSockJS(); // Sử dụng SockJS để hỗ trợ các trình duyệt không hỗ trợ WebSocket gốc
        // SockJS sẽ cung cấp các cơ chế fallback như long polling để đảm bảo kết nối WebSocket hoạt động trên nhiều trình duyệt khác nhau
        // Ví dụ: client có thể sử dụng thư viện SockJS để kết nối đến endpoint này
        //    var sockJsClientCode = """
        //       //    var socket = new SockJS('http://localhost:8080/ws');
        //       //       var socket = new SockJS('http://your-server-domain/ws');
        //      //       var stompClient = Stomp.over(socket);
        //     //       stompClient.connect({}, function(frame) {
        //    //           console.log('Connected: ' + frame);
        //   //           // Thực hiện các thao tác sau khi kết nối thành công
        //  //        });
        // //   """;
        //// Lưu ý: Đoạn code trên là ví dụ minh họa cách client có thể kết nối đến endpoint WebSocket sử dụng SockJS và STOMP
        // // Bạn cần tích hợp đoạn code này vào phần frontend của ứng dụng của bạn để thiết lập kết nối WebSocket
        // // Ngoài ra, bạn cũng cần xử lý các sự kiện kết nối, gửi và nhận tin nhắn trên client
        // // Đoạn code trên chỉ minh họa cách thiết lập kết nối ban đầu
        // // Hãy đảm bảo rằng bạn đã thêm các thư viện cần thiết cho SockJS và STOMP vào dự án frontend của bạn
        // // // Ví dụ: Sử dụng npm để cài đặt các thư viện này
        // // npm install sockjs-client stompjs
        // // // Sau đó, bạn có thể sử dụng chúng trong mã JavaScript của mình để thiết lập kết nối WebSocket
        // // // Lưu ý rằng bạn cần thay thế 'http://your-server-domain/ws' bằng URL thực tế của server nơi ứng dụng Spring Boot của bạn đang chạy
        // // // Ngoài ra, hãy đảm bảo rằng server của bạn đang chạy và có thể chấp nhận kết nối WebSocket tại endpoint /ws
        // // // Đoạn code trên chỉ là ví dụ minh họa và bạn cần điều chỉnh nó phù hợp với cấu trúc và logic của ứng dụng frontend của bạn
        // // // Hãy tham khảo tài liệu của SockJS và STOMP để biết thêm chi tiết về cách sử dụng chúng trong ứng dụng của bạn
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Cấu hình message broker đơn giản với các đích đến /topic và /queue
        registry.setApplicationDestinationPrefixes("/app"); // Thiết lập tiền tố cho các điểm đến ứng dụng
    }



}
