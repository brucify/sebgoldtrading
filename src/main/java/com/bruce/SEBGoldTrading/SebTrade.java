package com.bruce.SEBGoldTrading;

import java.sql.Timestamp;

public class SebTrade {

    private final double price;
    private final double volume;
    private final Timestamp timestamp;


    public SebTrade(double price, double volume) {
        this.price = price;
        this.volume = volume;

        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public double getPrice() {
        return price;
    }

    public double getVolume() {
        return volume;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
