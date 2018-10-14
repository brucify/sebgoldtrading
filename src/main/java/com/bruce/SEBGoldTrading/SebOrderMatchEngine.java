package com.bruce.SEBGoldTrading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class SebOrderMatchEngine implements Runnable {

    private static final int maxOrderQueueSize = 1000;

    private final SebOrderBook orderBook;
    private BlockingQueue<SebOrder> incomingOrders;
    private ConcurrentHashMap<Long, SebOrder> orders;

    @Autowired
    private SebOrderIdGenerator orderIdGenerator;

    private Thread mainThread;

    /**
     * Constructor
     */
    public SebOrderMatchEngine() {
        orders = new ConcurrentHashMap<>();
        incomingOrders = new ArrayBlockingQueue<>(maxOrderQueueSize);
        orderBook = new SebOrderBook();
    }

    public SebOrderBook getOrderBook() {
        return orderBook;
    }


    @Override
    public void run() {
        while (true) {
            while (!incomingOrders.isEmpty()) {
                process(incomingOrders.poll());
            }
        }
    }

    /**
     * Auto-starts the match engine after creation
     */
    @PostConstruct
    public void start() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        mainThread = new Thread(threadGroup, this);
        mainThread.start();
    }

    /**
     * Create a new order to the queue for processing, given side, price, and volume
     *
     * @param side
     * @param price
     * @param volume
     * @return
     */
    public SebOrder createOrder(SebOrder.Side side, double price, int volume) {
        Long orderId =  orderIdGenerator.next();
        SebOrder order = new SebOrder(side, price, volume, orderId);

        if (!incomingOrders.offer(order)) {
            try {
                incomingOrders.put(order);
            } catch (InterruptedException e) {
                order.setActionState(SebOrder.ActionState.INS_FAIL);
                return order;
            }
        }

        order.setActionState(SebOrder.ActionState.INS_PEND);
        return order;
    }

    /**
     * Process one order
     *
     * @param order
     */
    public void process(SebOrder order) {

        int remainingVolume = order.getVolume();

        while (remainingVolume != 0) {
            remainingVolume = processOrder(order.getSide(), order.getPrice(), remainingVolume);
        }

        order.setActionState(SebOrder.ActionState.INS_CONF);
        orders.put(order.getOrderId(), order);
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
