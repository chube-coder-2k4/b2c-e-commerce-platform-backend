package dev.commerce.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(org.springframework.web.socket.config.annotation.StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        ///    var sockJsClientCode = """
        //    var socket = new SockJS('http://localhost:8080/ws');
        //    var socket = new SockJS('http://your-server-domain/ws');
        //    var stompClient = Stomp.over(socket);
        //    stompClient.connect({}, function(frame) {
        //    console.log('Connected: ' + frame);
        //    // Thực hiện các thao tác sau khi kết nối thành công
        //    });
        //   """;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
