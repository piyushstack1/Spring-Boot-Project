package airbnb.example.airbnb.controller;


import airbnb.example.airbnb.dto.*;
import airbnb.example.airbnb.entity.enums.PaymentStatus;
import airbnb.example.airbnb.service.BookingService;
import airbnb.example.airbnb.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;
    private  final PaymentService paymentService;

    @PostMapping("/init")
    @Operation(summary = "Initiate the booking", tags = {"Booking Flow"})
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    @Operation(summary = "Add guest Ids to the booking", tags = {"Booking Flow"})
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId,
                                                @RequestBody List<Long> guestIdList) {
        return ResponseEntity.ok(bookingService.addGuests(bookingId, guestIdList));
    }


        @PostMapping("/{bookingId}/payment/initiate")
        @Operation(summary = "Initiate payments flow for the booking", tags = {"Booking Flow"})
        public ResponseEntity<Map<String, Object>> initiatePayment(
                @PathVariable Long bookingId) throws Exception {

            var payment = paymentService.initiatePayment(bookingId);

            return ResponseEntity.ok(Map.of(
                    "orderId", payment.getTransactionId(),
                    "amount", payment.getAmount(),
                    "currency", "INR"
            ));
        }

    @PostMapping("/payment/verify")
    @Operation(summary = "verifying the payment")
    public ResponseEntity<Map<String, String>> verifyPayment(
            @RequestBody RazorpayVerifyRequestDto request) throws Exception {

        paymentService.verifyPayment(
                request.getRazorpay_order_id(),
                request.getRazorpay_payment_id(),
                request.getRazorpay_signature()
        );

        return ResponseEntity.ok(
                Map.of("message", "Payment verified and booking confirmed")
        );
    }


    @PostMapping("/{bookingId}/cancel")
    @Operation(summary = "Cancel the booking", tags = {"Booking Flow"})
    public ResponseEntity<Map<String, String>> cancelBooking(
            @PathVariable Long bookingId) throws Exception {

        paymentService.refundPayment(bookingId);

        return ResponseEntity.ok(
                Map.of("message", "Booking cancelled successfully")
        );
    }

    @GetMapping("/{bookingId}/status")
    @Operation(summary = "Check the status of the booking", tags = {"Booking Flow"})
    public ResponseEntity<BookingStatusResponseDto> getBookingStatus(@PathVariable Long bookingId) {
        return ResponseEntity.ok(new BookingStatusResponseDto(bookingService.getBookingStatus(bookingId)));
    }

    @GetMapping("/payment/status/{orderId}")
    @Operation(summary = "Check the status of the payment", tags = {"Booking Flow"})
    public ResponseEntity<Map<String, PaymentStatus>> getPaymentStatus(
            @PathVariable String orderId) {

        return ResponseEntity.ok(
                Map.of("paymentStatus", paymentService.getPaymentStatus(orderId))
        );
    }

}
