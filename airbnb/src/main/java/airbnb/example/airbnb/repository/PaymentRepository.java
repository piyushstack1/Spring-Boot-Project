package airbnb.example.airbnb.repository;

import airbnb.example.airbnb.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Object> findByBookingId(Long bookingId);

    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

}
