package com.example;

/**
 * Credit card payment implementation extending PaymentProcessor.
 */
public class CreditCardPayment extends PaymentProcessor {
    private String cardNumber;
    private String cardHolder;

    public CreditCardPayment(double processingFee, String cardNumber, String cardHolder) {
        super(processingFee);
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    @Override
    public void processPayment(double amount) {
        double total = calculateTotal(amount);
        System.out.println("Processing payment of " + total + " from " + cardHolder);
    }

    public boolean validateCard() {
        return cardNumber != null && cardNumber.length() == 16;
    }
}
