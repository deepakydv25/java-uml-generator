package com.example;

/**
 * Payment gateway implementation.
 */
public interface IPaymentGateway {
    void processPayment(double amount);
    boolean validateCard(String cardNumber);
    void refund(String transactionId);
}
