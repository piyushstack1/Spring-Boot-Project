package airbnb.example.airbnb.entity;


import airbnb.example.airbnb.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Payment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "transaction_id", unique = true , nullable = false)
    private String transactionId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false,precision = 10 , scale = 2)
    private BigDecimal amount ;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "razorpay_signature")
    private String razorpaySignature;


    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    @OneToOne(fetch = FetchType.LAZY)
    private  Booking booking ;
}
