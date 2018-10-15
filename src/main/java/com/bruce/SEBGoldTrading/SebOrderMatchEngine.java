package com.bruce.SEBGoldTrading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class SebOrderMatchEngine implements Runnable {

    private static final int maxOrderQueueSize = 1000;

    private final SebOrderBook orderBook;
    private BlockingQueue<SebOrder> incomingOrders;
    private ConcurrentHashMap<Long, SebOrder> orders;
    private List<SebTrade> trades;


    @Autowired
    private SebOrderIdGenerator orderIdGenerator;

    private Thread mainThread;

    /**
     * Constructor
     */
    public SebOrderMatchEngine() {
        orderBook = new SebOrderBook();
        incomingOrders = new ArrayBlockingQueue<>(maxOrderQueueSize);
        orders = new ConcurrentHashMap<>();
        trades = new ArrayList<>();
    }

    public SebOrderBook getOrderBook() {
        return orderBook;
    }

    public ConcurrentHashMap<Long, SebOrder> getOrders() {
        return orders;
    }

    public List<SebTrade> getTrades() {
        return trades;
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
    private void process(SebOrder order) {

        Integer unprocessedVolume = order.getVolume();

        while (unprocessedVolume != 0) {
            unprocessedVolume = processOrder(order, unprocessedVolume);
        }

        order.setActionState(SebOrder.ActionState.INS_CONF);
        orders.put(order.getOrderId(), order);
    }

    /**
     * Process the order immediately at the given price and given volume,
     * modify the order book. Return the volume that remains un-executed
     *
     *
     * @param order
     * @param volume volume to be processed
     * @return remaining volume that is un-executed
     */
    private Integer processOrder(SebOrder order, int volume) {

        double price = order.getPrice();

        switch (order.getSide()) {
            case BUY:

                double bestAsk = orderBook.getBestAsk();

                if ( price < bestAsk || bestAsk == 0) {
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
                    Integer executedVolume = orderBook.executeBuyOrder(bestAsk, volume);

                    SebTrade trade = new SebTrade(bestAsk, executedVolume);
                    trades.add(trade);

                    return volume - executedVolume;
                }
            case SELL:

                double bestBid = orderBook.getBestBid();

                if ( bestBid < price || bestBid == 0) {
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
                    Integer executedVolume = orderBook.executeSellOrder(bestBid, volume);

                    SebTrade trade = new SebTrade(bestBid, executedVolume);
                    trades.add(trade);

                    return volume - executedVolume;
                }
            default:
                return 0;
        }

    }

}
