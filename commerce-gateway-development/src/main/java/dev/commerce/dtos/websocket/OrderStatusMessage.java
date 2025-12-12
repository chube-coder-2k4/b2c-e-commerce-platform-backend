package dev.commerce.dtos.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatusMessage {
    private String orderId;
    private String status;
    private String message;
}