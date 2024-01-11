package com.paymentgateway.Controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    @PostMapping("/create-payment-intent")
    public String createPaymentIntent(@RequestParam Integer amount) {
        Stripe.apiKey = stripeSecretKey;

        try {
            PaymentIntent intent = PaymentIntent.create(
                    new PaymentIntentCreateParams.Builder()
                            .setCurrency("usd")
                            .setAmount((long) amount*100) // Amount in cents, change accordingly
                            .build()
            );

            return generateResponse(intent.getClientSecret());
        } catch (StripeException e) {

            return generateResponse("Error creating PaymentIntent"+e.getMessage());
        }
    }

    private String generateResponse(String clientSecret) {

        return "{\"clientSecret\":\""+clientSecret+"\"}";
    }
}

