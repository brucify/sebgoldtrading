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

    private final Side side;
    private final double price;
    private final int volume;

    private final long orderId;
    private ActionState actionState;


    public SebOrder(Side side, double price, int volume, long orderId) {
        this.side = side;
        this.price = price;
        this.volume = volume;
        this.orderId = orderId;
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

    public void setActionState(ActionState actionState) {
        this.actionState = actionState;
    }
}
