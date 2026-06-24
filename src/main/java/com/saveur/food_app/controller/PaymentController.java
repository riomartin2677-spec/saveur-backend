package com.saveur.food_app.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = {"http://localhost:3000", "https://saveur-frontend.vercel.app"})
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    // Create Razorpay order
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> req) {
        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            double amount = Double.parseDouble(req.get("amount").toString());
            int amountInPaise = (int)(amount * 100); // Razorpay needs paise

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_" + System.currentTimeMillis());

            Order order = client.orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", amountInPaise);
            response.put("currency", "INR");
            response.put("keyId", keyId);
            return response;

        } catch (RazorpayException e) {
            throw new RuntimeException("Payment error: " + e.getMessage());
        }
    }

    // Verify payment after success
    @PostMapping("/verify")
    public Map<String, String> verifyPayment(@RequestBody Map<String, String> req) {
        try {
            String orderId = req.get("razorpay_order_id");
            String paymentId = req.get("razorpay_payment_id");
            String signature = req.get("razorpay_signature");

            String data = orderId + "|" + paymentId;
            String generatedSignature = hmacSHA256(data, keySecret);

            Map<String, String> response = new HashMap<>();
            if (generatedSignature.equals(signature)) {
                response.put("status", "success");
                response.put("paymentId", paymentId);
            } else {
                response.put("status", "failed");
            }
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Verification error: " + e.getMessage());
        }
    }

    private String hmacSHA256(String data, String secret) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec secretKey =
            new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}