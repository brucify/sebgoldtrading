package com.bruce.SEBGoldTrading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SebOrderDispatcher {

    @Autowired
    private SebOrderMatchEngine matchEngine;

    public SebOrderDispatcher() { }


    /**
     * Receive a new order
     *
     * @param side
     * @param price
     * @param volume
     * @return the order state
     */
    public SebOrder.ActionState newOrder(SebOrder.Side side, double price, int volume) {

        matchEngine.process(side, price, volume);

        return SebOrder.ActionState.INS_PEND;

    }

}