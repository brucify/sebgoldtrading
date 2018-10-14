package com.bruce.SEBGoldTrading;

import org.springframework.stereotype.Service;



@Service
public class SebOrderMatchEngine {

    private final SebOrderBook orderBook;

    public SebOrderMatchEngine() {
        orderBook = new SebOrderBook();
    }

    public SebOrderBook getOrderBook() {
        return orderBook;
    }

    public void process(SebOrder.Side side, double price, int volume) {

        int remainingVolume = volume;

        while (remainingVolume != 0) {
            remainingVolume = processOrder(side, price, remainingVolume);
        }
    }

    /**
     * Process the order immediately at the given price and given volume,
     * modify the order book
     *
     * @param side
     * @param price
     * @param volume
     * @return remaining volume that is un-executed
     */
    private Integer processOrder(SebOrder.Side side, double price, int volume) {

        switch (side) {
            case BUY:

                double bestAsk = orderBook.getBestAsk();

                if ( price < bestAsk ) {
                    /*
                     *  If new bid price is lower than the lowest ask,
                     *  just add it to the bid depth
                     **/
                    orderBook.addToBidDepth(price, volume); // no trade
                    return 0;
                } else {
                    /*
                     *  Else if bid price is equal or greater than lowest ask,
                     *  execute the order
                     **/
                    return orderBook.executeBuyOrder(bestAsk, volume);
                }
            case SELL:

                double bestBid = orderBook.getBestBid();

                if ( bestBid < price ) {
                    /*
                     *  If new ask price is higher than the highest bid,
                     *  just add it to the ask depth
                     **/
                    orderBook.addToAskDepth(price, volume); // no trade
                    return 0;
                } else {
                    /*
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
