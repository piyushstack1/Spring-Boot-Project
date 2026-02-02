package airbnb.example.airbnb.entity.enums;

public enum PaymentStatus {
    CREATED,    // Razorpay order created, payment not done yet
    PAID,       // Payment successfully completed
    FAILED,     // Payment attempt failed
    REFUNDED
}
