package com.bruce.SEBGoldTrading;

public class SebOrderMatchEngine {

    enum Side {
        BUY, SELL
    }

    enum OrderState {
        INS_CONF, INS_FAIL, //INS_PEND,
        DEL_CONF, DEL_FAIL  //, DEL_PEND
    }

    private final SebOrderDepth orderDepth;

    public SebOrderMatchEngine() {
        orderDepth = new SebOrderDepth();
    }

    public SebOrderDepth getOrderDepth() {
        return orderDepth;
    }

    public OrderState insertOrder(Side side, double price, int volume) {
        if (true) {
            //orderDepth.insertOrder(price, volume);
            return OrderState.INS_CONF;
        } else {
            return OrderState.INS_FAIL;
        }
    }

//    public OrderState deleteOrder(double price, int volume) {
//        if (true) {
//            orderDepth.deleteOrder(price, volume);
//            return OrderState.DEL_CONF;
//        } else {
//            return OrderState.DEL_FAIL;
//        }
//    }
}
