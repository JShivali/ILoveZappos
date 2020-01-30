package com.example.cryptoappzappos.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for data from price alert API.
 */

public class PriceCheck {

    public float getLast() {
        return last;
    }

    public void setLast(float last) {
        this.last = last;
    }

    @SerializedName("last")
    private float last;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("bid")
    private float bid;

    @SerializedName("vwap")
    private float vwap;

    @SerializedName("volume")
    private double volume;


    @SerializedName("low")
    private float low;

    @SerializedName("ask")
    private float ask;

    @SerializedName("open")
    private float open;

    @SerializedName("high")
    private float high;
}
