package com.bruce.SEBGoldTrading;

import java.util.Map;

public class SebDepthResponse {

    private final long id;
    private final SebOrderBook orderBook;

    public SebDepthResponse(long id, SebOrderBook orderBook) {
        this.id = id;
        this.orderBook = orderBook;
    }

    public long getId() {
        return this.id;
    }

    public Map getAllBids() {
        return orderBook.getAllBids();
    }

    public Map getAllAsks() {
        return orderBook.getAllAsks();
    }

    public double getBestBid() {
        return orderBook.getBestBid();
    }

    public double getBestAsk() {
        return orderBook.getBestAsk();
    }
}