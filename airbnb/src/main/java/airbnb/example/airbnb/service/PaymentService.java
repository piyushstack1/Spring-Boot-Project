package airbnb.example.airbnb.service;

import airbnb.example.airbnb.entity.Payment;
import airbnb.example.airbnb.entity.enums.PaymentStatus;
import com.razorpay.RazorpayException;

public interface PaymentService {

    // Create Razorpay order & initiate payment
    Payment initiatePayment(Long bookingId) throws RazorpayException;

    // Verify Razorpay payment after success
    void verifyPayment(String orderId, String paymentId, String signature) throws Exception;

    // Get payment status using Razorpay orderId
    PaymentStatus getPaymentStatus(String orderId);

    // Cancel booking & refund payment (if already paid)
    void refundPayment(Long bookingId) throws RazorpayException;


//    void handleWebhookSuccess(String payload);
//
//    void handleWebhookFailure(String payload);
//
//    void handleRefundWebhook(String payload);
}
