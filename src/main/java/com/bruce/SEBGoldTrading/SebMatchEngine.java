package com.bruce.SEBGoldTrading;

import org.springframework.stereotype.Service;

@Service
public class SebMatchEngine {

    enum Side {
        BUY, SELL
    }

    enum OrderState {
        INS_CONF, INS_FAIL, INS_PEND,
        DEL_CONF, DEL_FAIL, DEL_PEND
    }

    private final SebOrderBook orderBook;

    public SebMatchEngine() {
        orderBook = new SebOrderBook();
    }

    public SebOrderBook getOrderBook() {
        return orderBook;
    }

    public OrderState newOrder(Side side, double price, int volume) {

        switch (side) {
            case BUY:
                /**
                 *  If new bid price is lower than the lowest ask, just add it to the bid depth
                 **/
                if (price < orderBook.getBestAsk()) {
                    return addToBidDepth(price, volume); // no trade
                }
                /**
                 *  Else try to execute a quantity of the order
                 **/
                else {
                    // TODO
                    return OrderState.INS_FAIL;
                }
            case SELL:
                /**
                 *  If new ask price is higher than the highest bid, just add it to the ask depth
                 **/
                if (orderBook.getBestBid() < price) {
                    return addToAskDepth(price, volume); // no trade
                }
                /**
                 *  Else try to execute a quantity of the order
                 **/
                else {
                    // TODO
                    return OrderState.INS_FAIL;
                }
            default:
                return OrderState.INS_FAIL;
        }


    }

    private OrderState addToBidDepth(double price, int volume) {
        Integer currentBidVolume = orderBook.getAllBids().get(price);
        if (currentBidVolume == null)
            orderBook.setOneBid(price, volume);
        else
            orderBook.setOneBid(price, volume + currentBidVolume);
        return OrderState.INS_CONF;
    }

    private OrderState addToAskDepth(double price, int volume) {
        Integer currentAskVolume = orderBook.getAllAsks().get(price);
        if (currentAskVolume == null)
            orderBook.setOneAsk(price, volume);
        else
            orderBook.setOneAsk(price, volume + currentAskVolume);
        return OrderState.INS_CONF;
    }

    private OrderState executeOrder(double price, int volume) {
        // TODO
        return OrderState.INS_FAIL;
    }
}