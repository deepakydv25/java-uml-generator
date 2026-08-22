package com.example;

/**
 * Base payment processor class.
 */
public abstract class PaymentProcessor {
    protected double processingFee;

    public PaymentProcessor(double processingFee) {
        this.processingFee = processingFee;
    }

    public abstract void processPayment(double amount);

    protected double calculateTotal(double amount) {
        return amount + processingFee;
    }
}

