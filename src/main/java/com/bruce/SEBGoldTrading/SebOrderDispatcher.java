package com.bruce.SEBGoldTrading;

import org.springframework.stereotype.Service;

@Service
public class SebOrderDispatcher {

    public enum Side {
        BUY, SELL
    }

    /**
     * State of the insert or delete actions
     */
    public enum ActionState {
        INS_CONF, INS_FAIL, INS_PEND,
        DEL_CONF, DEL_FAIL, DEL_PEND
    }

    /**
     * State of the order
     */
    enum OrderState {
        DELETED,
        ON_MARKET
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
    public ActionState newOrder(Side side, double price, int volume) {

        int remainingVolume = volume;

        while (remainingVolume != 0) {
            remainingVolume = processOrder(side, price, remainingVolume);
        }

        return ActionState.INS_CONF;

    }

    private Integer processOrder(Side side, double price, int volume) {

        switch (side) {
            case BUY:

                double bestAsk = orderBook.getBestAsk();

                if ( price < bestAsk ) {
                    /**
                     *  If new bid price is lower than the lowest ask,
                     *  just add it to the bid depth
                     **/
                    orderBook.addToBidDepth(price, volume); // no trade
                    return 0;
                } else {
                    /**
                     *  Else if bid price is equal or greater than lowest ask,
                     *  execute the order
                     **/
                    return orderBook.executeBuyOrder(bestAsk, volume);
                }
            case SELL:

                double bestBid = orderBook.getBestBid();

                if ( bestBid < price ) {
                    /**
                     *  If new ask price is higher than the highest bid,
                     *  just add it to the ask depth
                     **/
                    orderBook.addToAskDepth(price, volume); // no trade
                    return 0;
                } else {
                    /**
                     *  Else if ask price is equal or lower than highest ask,
                     *  execute the order
                     **/
                    return orderBook.executeSellOrder(bestBid, volume);
                }
            default:
                return 0;
        }

    }
}