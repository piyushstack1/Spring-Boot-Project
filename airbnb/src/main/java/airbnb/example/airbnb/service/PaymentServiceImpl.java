package airbnb.example.airbnb.service;

import airbnb.example.airbnb.entity.Booking;
import airbnb.example.airbnb.entity.Payment;
import airbnb.example.airbnb.entity.enums.BookingStatus;
import airbnb.example.airbnb.entity.enums.PaymentStatus;
import airbnb.example.airbnb.repository.BookingRepository;
import airbnb.example.airbnb.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClient razorpayClient;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    // ================= INITIATE PAYMENT =================
    @Override
    public Payment initiatePayment(Long bookingId) throws RazorpayException {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.GUESTS_ADDED) {
            throw new RuntimeException("Booking not ready for payment");
        }

        JSONObject options = new JSONObject();
        options.put(
                "amount",
                booking.getAmount()
                        .multiply(BigDecimal.valueOf(100))
                        .longValue()
        );
        options.put("currency", "INR");
        options.put("receipt", "booking_" + bookingId);

        Order order = razorpayClient.orders.create(options);

        Payment payment = new Payment();
        payment.setTransactionId(order.get("id")); // razorpay_order_id
        payment.setAmount(booking.getAmount());
        payment.setPaymentStatus(PaymentStatus.CREATED);
        payment.setBooking(booking);

        booking.setBookingStatus(BookingStatus.PAYMENTS_PENDING);
        bookingRepository.save(booking);

        return paymentRepository.save(payment);
    }

    // ================= VERIFY PAYMENT =================
    @Override
    @Transactional
    public void verifyPayment(String orderId, String paymentId, String signature)
            throws Exception {

//


        Payment payment = paymentRepository.findByTransactionId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));


        payment.setRazorpayPaymentId(paymentId);
        payment.setRazorpaySignature(signature);
        paymentRepository.save(payment);


        String data = orderId + "|" + paymentId;
        String generatedSignature = generateSignature(data, razorpaySecret);

        if (!generatedSignature.equals(signature)) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.getBooking().setBookingStatus(BookingStatus.PAYMENTS_PENDING);
            paymentRepository.save(payment);
            return;
        }


        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.getBooking().setBookingStatus(BookingStatus.CONFIRMED);
        paymentRepository.save(payment);
    }


    // ================= GET PAYMENT STATUS =================
    @Override
    public PaymentStatus getPaymentStatus(String orderId) {
        return paymentRepository.findByTransactionId(orderId)
                .map(Payment::getPaymentStatus)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    // ================= CANCEL / REFUND =================
    @Override
    public void refundPayment(Long bookingId) throws RazorpayException {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = (Payment) paymentRepository
                .findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment != null && payment.getPaymentStatus() == PaymentStatus.PAID) {
            razorpayClient.payments.refund(payment.getRazorpayPaymentId());
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }



    // ================= SIGNATURE UTILITY =================
    private String generateSignature(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
        return Hex.encodeHexString(mac.doFinal(data.getBytes()));
    }




}
