package com.bruce.SEBGoldTrading;

import org.springframework.stereotype.Service;

@Service
public class SebOrderDispatcher {

    enum Side {
        BUY, SELL
    }

    enum OrderState {
        INS_CONF, INS_FAIL, INS_PEND,
        DEL_CONF, DEL_FAIL, DEL_PEND
    }

    private final SebOrderBook orderBook;

    public SebOrderDispatcher() {
        orderBook = new SebOrderBook();
    }

    public SebOrderBook getOrderBook() {
        return orderBook;
    }

    /**
     * Process a new order
     *
     * @param side
     * @param price
     * @param volume
     * @return the order state
     */
    public OrderState newOrder(Side side, double price, int volume) {

        int remainingVolume = volume;

        while (remainingVolume != 0) {
            remainingVolume = processOrder(side, price, remainingVolume);
        }

        return OrderState.INS_CONF;

    }

    private Integer processOrder(Side side, double price, int volume) {

        switch (side) {
            case BUY:
                /**
                 *  If new bid price is lower than the lowest ask,
                 *  just add it to the bid depth
                 **/
                double bestAsk = orderBook.getBestAsk();

                if ( price < bestAsk ) {
                    orderBook.addToBidDepth(price, volume); // no trade
                    return 0;
                }
                /**
                 *  Else if bid price equals or greater than lowest ask,
                 *  execute the order
                 **/
                else {
                    return orderBook.executeBuyOrder(bestAsk, volume);
                }
            case SELL:
                /**
                 *  If new ask price is higher than the highest bid,
                 *  just add it to the ask depth
                 **/
                double bestBid = orderBook.getBestBid();
                if ( bestBid < price ) {
                    orderBook.addToAskDepth(price, volume); // no trade
                    return 0;
                }
                /**
                 *  Else try to execute a quantity of the order
                 **/
                else {
                    return orderBook.executeSellOrder(bestBid, volume);
                }
            default:
                return 0;
        }

    }
}