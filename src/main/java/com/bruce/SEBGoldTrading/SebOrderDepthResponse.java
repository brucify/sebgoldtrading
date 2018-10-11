package com.bruce.SEBGoldTrading;

import java.util.Map;

public class SebOrderDepthResponse {

    private final long id;
    private final SebOrderDepth orderDepth;

    public SebOrderDepthResponse(long id, SebOrderDepth orderDepth) {
        this.id = id;
        this.orderDepth = orderDepth;
    }

    public long getId() {
        return this.id;
    }

    public Map getAllBids() {
        return orderDepth.getAllBids();
    }

    public Map getAllAsks() {
        return orderDepth.getAllAsks();
    }

    public double getBestBid() {
        return orderDepth.getBestBid();
    }

    public double getBestAsk() {
        return orderDepth.getBestAsk();
    }
}