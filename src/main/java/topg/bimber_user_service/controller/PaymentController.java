package topg.bimber_user_service.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {


    static {
        Stripe.apiKey = "your-secret-key";
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> payload) {
        // Extract amount from the request body
        Long amount = (Long) payload.get("amount");

        if (amount == null || amount!=0L) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }

        try {
            // Create PaymentIntent parameters
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(Long.valueOf(amount)) // Amount in cents
                    .setCurrency("gbp") // Currency
                    .build();

            // Create the PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Return the client secret to the frontend
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            // Handle Stripe errors
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}