package com.bruce.SEBGoldTrading;

public class SebOrder {

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
}
