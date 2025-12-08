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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
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
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orders not found"));

        // Kiểm tra orders thuộc về user hiện tại
        if (!orders.getUsers().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to pay for this orders");
        }

        // Kiểm tra trạng thái orders
        if (orders.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Orders is not in pending status");
        }

        // Tạo payment record
        Payment payment = Payment.builder()
                .orders(orders)
                .provider(orders.getPaymentMethod())
                .amount(orders.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .build();
        payment.setCreatedBy(user.getId());
        paymentRepository.save(payment);

        // Tạo VNPay URL
        String vnp_TxnRef = payment.getId().toString();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf((long)(orders.getTotalAmount() * 100)));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + orders.getOrderCode());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        String queryUrl = VNPayUtil.hashAllFields(vnp_Params);
        String vnp_SecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), queryUrl);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnPayConfig.getUrl() + "?" + queryUrl;

        return new PaymentUrlResponse(
                paymentUrl,
                orders.getOrderCode(),
                user.getId(),
                user.getId()
        );
    }

    @Override
    @Transactional
    public PaymentResponse handlePaymentCallback(Map<String, String> vnpParams) {
        String vnp_SecureHash = vnpParams.get("vnp_SecureHash");
        vnpParams.remove("vnp_SecureHashType");
        vnpParams.remove("vnp_SecureHash");

        // Verify signature
        String signValue = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), VNPayUtil.hashAllFields(vnpParams));

        if (!signValue.equals(vnp_SecureHash)) {
            throw new IllegalArgumentException("Invalid payment signature");
        }

        UUID paymentId = UUID.fromString(vnpParams.get("vnp_TxnRef"));
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        String responseCode = vnpParams.get("vnp_ResponseCode");
        String transactionNo = vnpParams.get("vnp_TransactionNo");

        Orders orders = payment.getOrders();

        if ("00".equals(responseCode)) {
            // Thanh toán thành công
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setTransactionId(transactionNo);
            payment.setPaidAt(LocalDateTime.now());

            orders.setStatus(OrderStatus.PAID);
            orders.setPaidAt(LocalDateTime.now());
        } else {
            // Thanh toán thất bại
            payment.setStatus(PaymentStatus.FAILED);
            orders.setStatus(OrderStatus.PAYMENT_FAILED);
        }

        paymentRepository.save(payment);
        orderRepository.save(orders);

        return paymentMapper.toResponse(payment);
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
