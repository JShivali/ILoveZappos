package com.example.cryptoappzappos.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Model class for data from Transaction API.
 */

public class Transaction {

    @SerializedName("tid")
    private int tid;
    @SerializedName("date")
    private String date;
    @SerializedName("price")
    private float price;

    @SerializedName("type")
    private String type;

    @SerializedName("amount")
    private double amount;


    public int gettid() {
        return tid;
    }

    public void settid(int transactionId) {
        this.tid = transactionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    private String text;

}
