package airbnb.example.airbnb.dto;

public class RazorpayVerifyRequestDto {
    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String razorpay_signature;

    public String getRazorpay_order_id() {
        return razorpay_order_id;
    }

    public String getRazorpay_payment_id() {
        return razorpay_payment_id;
    }

    public String getRazorpay_signature() {
        return razorpay_signature;
    }
}
