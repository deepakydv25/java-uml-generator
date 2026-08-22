package com.example;

public class Order {
    private PaymentGateway gateway;
    private double amount;
    private String orderId;

    public Order(PaymentGateway gateway) {
        this.gateway = gateway;
        this.amount = 0.0;
    }

    public void checkout() {
        gateway.processPayment(100.0);
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
