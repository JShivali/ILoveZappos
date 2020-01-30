package com.example.cryptoappzappos.Model;

/**
 *
 */

public class Order {

    private double sum;
    private double value;
    private double amount;
    private double bid;
    private boolean type;


    public Order(double sum, double value, double amount, double bid, boolean type) {
        this.sum = sum;
        this.value = value;
        this.amount = amount;
        this.bid = bid;
        this.type = type;
    }

    public Order() {

    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }


}
