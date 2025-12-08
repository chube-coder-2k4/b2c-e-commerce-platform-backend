package dev.commerce.services.impl;

import dev.commerce.configurations.VNPayConfig;
import dev.commerce.dtos.common.OrderStatus;
import dev.commerce.dtos.common.PaymentStatus;
import dev.commerce.dtos.response.PaymentResponse;
import dev.commerce.dtos.response.PaymentUrlResponse;
import dev.commerce.entitys.Orders;
import dev.commerce.entitys.Payment;
import dev.commerce.entitys.Users;
import dev.commerce.exception.ResourceNotFoundException;
import dev.commerce.mappers.PaymentMapper;
import dev.commerce.repositories.jpa.OrderRepository;
import dev.commerce.repositories.jpa.PaymentRepository;
import dev.commerce.services.PaymentService;
import dev.commerce.utils.AuthenticationUtils;
import dev.commerce.utils.VNPayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final AuthenticationUtils utils;
    private final VNPayConfig vnPayConfig;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentUrlResponse createPaymentUrl(UUID orderId) {
        Users user = utils.getCurrentUser();
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Kiểm tra order thuộc về user hiện tại
        if (!order.getUsers().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to pay for this order");
        }

        // Kiểm tra trạng thái order
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Order is not in pending status");
        }

        // Tạo payment record
        Payment payment = Payment.builder()
                .orders(order)
                .provider(order.getPaymentMethod())
                .amount(order.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .build();
        payment.setCreatedBy(user.getId());
        paymentRepository.save(payment);

        // Tạo VNPay URL - SỬ DỤNG buildPaymentUrl từ VNPayUtil
        String paymentUrl = VNPayUtil.buildPaymentUrl(payment.getId(), order.getTotalAmount(), vnPayConfig);

        log.info("=== VNPAY PAYMENT URL DEBUG ===");
        log.info("Payment ID: {}", payment.getId());
        log.info("Amount: {}", order.getTotalAmount());
        log.info("Payment URL: {}", paymentUrl);
        log.info("===============================");

        return new PaymentUrlResponse(
                paymentUrl,
                order.getOrderCode(),
                user.getId(),
                user.getId()
        );
    }

    @Override
    @Transactional
    public PaymentResponse handlePaymentCallback(Map<String, String> vnpParams) {
        String vnp_SecureHash = vnpParams.get("vnp_SecureHash");
        vnpParams.remove("vnp_SecureHash");
        vnpParams.remove("vnp_SecureHashType");

        String signValue = VNPayUtil.hashAllFields(vnpParams);
        String vnp_SecureHash_new = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), signValue);

        if (vnp_SecureHash.equals(vnp_SecureHash_new)) {
            UUID paymentId = UUID.fromString(vnpParams.get("vnp_TxnRef"));
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

            String responseCode = vnpParams.get("vnp_ResponseCode");
            String transactionNo = vnpParams.get("vnp_TransactionNo");

            Orders order = payment.getOrders();

            if ("00".equals(responseCode)) {
                payment.setStatus(PaymentStatus.COMPLETED);
                payment.setTransactionId(transactionNo);
                payment.setPaidAt(LocalDateTime.now());

                order.setStatus(OrderStatus.PAID);
                order.setPaidAt(LocalDateTime.now());
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                order.setStatus(OrderStatus.PAYMENT_FAILED);
            }

            paymentRepository.save(payment);
            orderRepository.save(order);

            return paymentMapper.toResponse(payment);
        } else {
            throw new IllegalArgumentException("Invalid payment signature");
        }
    }

    @Override
    public List<PaymentResponse> getMyPayments() {
        Users user = utils.getCurrentUser();
        List<Orders> userOrders = orderRepository.findByUsers(user);
        List<UUID> orderIds = userOrders.stream().map(Orders::getId).toList();

        return paymentRepository.findByOrdersIdIn(orderIds)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    public PaymentResponse getPaymentDetails(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        Users user = utils.getCurrentUser();
        if (!payment.getOrders().getUsers().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to view this payment");
        }

        return paymentMapper.toResponse(payment);
    }
}
