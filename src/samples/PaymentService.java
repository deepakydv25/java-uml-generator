package com.example;

/**
 * Payment service that implements the payment gateway interface.
 */
public class PaymentService implements IPaymentGateway {
    private CreditCardPayment creditCardPayment;
    private String serviceName;

    public PaymentService(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public void processPayment(double amount) {
        if (creditCardPayment != null) {
            creditCardPayment.processPayment(amount);
        }
    }

    @Override
    public boolean validateCard(String cardNumber) {
        return cardNumber != null && cardNumber.length() > 0;
    }

    @Override
    public void refund(String transactionId) {
        System.out.println("Processing refund for transaction: " + transactionId);
    }

    public void setCreditCardPayment(CreditCardPayment creditCardPayment) {
        this.creditCardPayment = creditCardPayment;
    }
}
