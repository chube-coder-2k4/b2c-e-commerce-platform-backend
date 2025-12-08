package dev.commerce.controllers;

import dev.commerce.dtos.response.PaymentResponse;
import dev.commerce.dtos.response.PaymentUrlResponse;
import dev.commerce.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@Validated
@Slf4j
@Tag(name = "Payment Controller", description = "APIs for managing payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Create payment URL", description = "Creates a payment URL for an orders to redirect user to VNPay")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment URL created successfully"),
            @ApiResponse(responseCode = "404", description = "Orders not found"),
            @ApiResponse(responseCode = "400", description = "Invalid orders status or unauthorized")
    })
    @PostMapping("/create/{orderId}")
    public ResponseEntity<PaymentUrlResponse> createPaymentUrl(@PathVariable UUID orderId) {
        log.info("Creating payment URL for orders: {}", orderId);
        PaymentUrlResponse response = paymentService.createPaymentUrl(orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "VNPay callback", description = "Handles callback from VNPay after payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid signature or payment failed")
    })
    @GetMapping("/vnpay-callback")
    public void handleVNPayCallback(@RequestParam Map<String, String> vnpParams,
                                     HttpServletResponse response) throws IOException {
        log.info("Received VNPay callback with params: {}", vnpParams);
        try {
            PaymentResponse paymentResponse = paymentService.handlePaymentCallback(vnpParams);

            // Redirect dựa trên kết quả thanh toán
            if (paymentResponse.status().toString().equals("COMPLETED")) {
                response.sendRedirect("/payment-success?orderId=" + paymentResponse.orderId());
            } else {
                response.sendRedirect("/payment-failed?orderId=" + paymentResponse.orderId());
            }
        } catch (Exception e) {
            log.error("Error processing VNPay callback: ", e);
            response.sendRedirect("/payment-failed?error=" + e.getMessage());
        }
    }

    @Operation(summary = "Get my payments", description = "Retrieves all payments of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/my-payments")
    public ResponseEntity<List<PaymentResponse>> getMyPayments() {
        return ResponseEntity.ok(paymentService.getMyPayments());
    }

    @Operation(summary = "Get payment details", description = "Retrieves detailed information about a specific payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized to view this payment")
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentDetails(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentDetails(paymentId));
    }
}

