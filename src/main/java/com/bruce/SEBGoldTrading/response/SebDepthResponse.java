package com.bruce.SEBGoldTrading.response;

import com.bruce.SEBGoldTrading.SebOrderBook;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(value = HttpStatus.OK)
public class SebDepthResponse {

    private final SebOrderBook orderBook;

    public SebDepthResponse(SebOrderBook orderBook) {
        this.orderBook = orderBook;
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