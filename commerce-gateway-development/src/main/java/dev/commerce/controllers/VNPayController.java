package dev.commerce.controllers;

import dev.commerce.configurations.VNPayConfig;
import dev.commerce.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping("/vnpay")
public class VNPayController {

    @Autowired
    private VNPayConfig vnPayConfig;

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestParam Long amount,
                                           @RequestParam String bankCode,
                                           @RequestParam String language,
                                           HttpServletRequest request) throws UnsupportedEncodingException {

        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = request.getRemoteAddr();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", language);
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        String queryUrl = VNPayUtil.hashAllFields(vnp_Params);
        String vnp_SecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), queryUrl);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnPayConfig.getUrl() + "?" + queryUrl;

        Map<String, String> response = new HashMap<>();
        response.put("code", "00");
        response.put("data", paymentUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/return")
    public String paymentReturn(@RequestParam Map<String, String> params) {
        String vnp_SecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHashType");
        params.remove("vnp_SecureHash");

        String signValue = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), VNPayUtil.hashAllFields(params));

        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(params.get("vnp_TransactionStatus"))) {
                return "redirect:/payment-success";
            }
        }
        return "redirect:/payment-failed";
    }

    @PostMapping("/ipn")
    public ResponseEntity<?> paymentIPN(@RequestParam Map<String, String> params) {
        String vnp_SecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHashType");
        params.remove("vnp_SecureHash");

        String signValue = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), VNPayUtil.hashAllFields(params));

        Map<String, String> response = new HashMap<>();
        if (signValue.equals(vnp_SecureHash)) {
            // Xử lý logic cập nhật database
            response.put("RspCode", "00");
            response.put("Message", "Confirm Success");
        } else {
            response.put("RspCode", "97");
            response.put("Message", "Invalid Checksum");
        }
        return ResponseEntity.ok(response);
    }
}
