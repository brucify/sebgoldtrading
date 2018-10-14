package com.bruce.SEBGoldTrading;

import java.sql.Timestamp;

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

    private final Side side;
    private final double price;
    private final int volume;

    private final long orderId;
    private ActionState actionState;
    private final Timestamp timestamp;


    public SebOrder(Side side, double price, int volume, long orderId) {
        this.side = side;
        this.price = price;
        this.volume = volume;
        this.orderId = orderId;

        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Side getSide() {
        return side;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public long getOrderId() {
        return orderId;
    }

    public ActionState getActionState() {
        return actionState;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setActionState(ActionState actionState) {
        this.actionState = actionState;
    }

}
