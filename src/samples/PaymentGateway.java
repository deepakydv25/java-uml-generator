package com.example;

public interface PaymentGateway {
    void processPayment(double amount);
    boolean validateCard(String cardNumber);
    void refund(String transactionId);
}
