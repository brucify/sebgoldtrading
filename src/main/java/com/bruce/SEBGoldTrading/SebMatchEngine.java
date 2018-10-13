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
                if ( price < orderBook.getBestAsk() ) {
                    orderBook.addToBidDepth(price, volume); // no trade
                    return SebMatchEngine.OrderState.INS_CONF;
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
                if ( orderBook.getBestBid() < price ) {
                    orderBook.addToAskDepth(price, volume); // no trade
                    return SebMatchEngine.OrderState.INS_CONF;
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

    private OrderState executeOrder(double price, int volume) {
        // TODO
        return OrderState.INS_FAIL;
    }
}