package dev.commerce.services.impl;

import dev.commerce.dtos.common.OrderStatus;
import dev.commerce.dtos.common.PaymentMethod;
import dev.commerce.dtos.request.OrderRequest;
import dev.commerce.dtos.response.OrderDetailResponse;
import dev.commerce.dtos.response.OrderResponse;
import dev.commerce.dtos.websocket.OrderStatusMessage;
import dev.commerce.entitys.*;
import dev.commerce.exception.ResourceNotFoundException;
import dev.commerce.mappers.OrderMapper;
import dev.commerce.repositories.jpa.*;
import dev.commerce.services.OrderService;
import dev.commerce.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthenticationUtils utils;
    private final SimpMessagingTemplate messagingTemplate;


    @Override
    public OrderDetailResponse createOrder(OrderRequest orderRequest) {
        Users user = utils.getCurrentUser();
        Cart cart = cartRepository.findByUsers(user).orElseThrow(() -> new ResourceNotFoundException("Cart is Empty"));
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if(cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is Empty");
        }
        Orders orders = new Orders();
        orders.setUsers(user);
        orders.setOrderCode("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        orders.setStatus(OrderStatus.PENDING);
        orders.setTotalAmount(cart.getTotalPrice());
        orders.setPaymentMethod(orderRequest.getPaymentMethod() != null ? orderRequest.getPaymentMethod() : PaymentMethod.COD);
        orders.setShippingAddress(orderRequest.getShippingAddress() != null ? orderRequest.getShippingAddress() : user.getAddress());
        orderRepository.save(orders);

        List<OrderItem> orderItem = cartItems.stream().map(ci -> {
            OrderItem item = new OrderItem();
            item.setOrders(orders);
            item.setProduct(ci.getProduct());
            item.setQuantity(ci.getQuantity());
            item.setUnitPrice(ci.getPrice());
            return orderItemRepository.save(item);
        }).toList();
        cartItemRepository.deleteAll(cartItems);
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        return orderMapper.toOrderDetailResponse(orders,orderItem);
    }

    @Override
    public List<OrderDetailResponse> getUserOrders() {
        Users user = utils.getCurrentUser();
        return orderRepository.findByUsers(user).stream().map(order -> {
            List<OrderItem> orderItems = orderItemRepository.findByOrders(order);
            return orderMapper.toOrderDetailResponse(order, orderItems);
        }).toList();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Orders> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::toOrderResponse).toList();
    }

    @Override
    public OrderResponse updateStatus(UUID orderId, OrderStatus status) {
        NotificationSocket noti = new NotificationSocket();

        Orders orders = getOrderById(orderId);
        orders.setStatus(status);
        orderRepository.save(orders);
        pushOrderStatusToUser(orders);
        return orderMapper.toOrderResponse(orders);
    }

    @Override
    public OrderResponse cancelOrder(UUID orderId) {
        Orders orders = getOrderById(orderId);
        orders.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(orders);
        return orderMapper.toOrderResponse(orders);

    }

    @Override
    public OrderDetailResponse getOrderDetails(UUID orderId) {
        Orders orders = getOrderById(orderId);
        List<OrderItem> orderItems = orderItemRepository.findByOrders(orders);
        return orderMapper.toOrderDetailResponse(orders, orderItems);
    }

    private Orders getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orders not found"));
    }

    private void pushOrderStatusToUser(Orders order) {
        String userId = order.getUsers().getId().toString();

        OrderStatusMessage payload = new OrderStatusMessage(
                order.getId().toString(),
                order.getStatus().name(),
                "Your order " + order.getOrderCode() + " is now " + order.getStatus()
        );

        messagingTemplate.convertAndSend(
                "/topic/order-status." + userId,
                payload
        );
    }


}
