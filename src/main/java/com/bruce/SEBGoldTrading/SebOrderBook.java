package com.bruce.SEBGoldTrading;

import java.util.Comparator;
import java.util.TreeMap;

public class SebOrderBook {

    private final TreeMap<Double, Integer> askDepth = new TreeMap<>();
    private final TreeMap<Double, Integer> bidDepth = new TreeMap<>(Comparator.reverseOrder());

    public SebOrderBook() {
        // fake order depth to begin with
        setOneBid(1300.0, 10);
        setOneBid(1299.5, 9);
        setOneBid(1299.0, 8);
        setOneBid(1298.5, 7);
        setOneBid(1298.0, 6);
        setOneBid(1297.5, 5);

        // fake order depth to begin with
        setOneAsk(1300.5, 10);
        setOneAsk(1301.0, 9);
        setOneAsk(1301.5, 8);
        setOneAsk(1302.0, 7);
        setOneAsk(1302.5, 6);
        setOneAsk(1303.0, 5);
    }

    public TreeMap<Double, Integer> getAllBids() {
        return bidDepth;
    }

    public TreeMap<Double, Integer> getAllAsks() {
        return askDepth;
    }

    public double getBestBid() {
        if (bidDepth.isEmpty()) return 0;

        return bidDepth.firstKey();
    }

    public double getBestAsk() {
        if (askDepth.isEmpty()) return 0;

        return askDepth.firstKey();
    }

    public void addToBidDepth(double price, int volume) {
        Integer oldVolume = bidDepth.get(price);
        if (oldVolume == null)
            setOneBid(price, volume);
        else
            setOneBid(price, volume + oldVolume);
    }

    public void addToAskDepth(double price, int volume) {
        Integer oldVolume = askDepth.get(price);
        if (oldVolume == null)
            setOneAsk(price, volume);
        else
            setOneAsk(price, volume + oldVolume);
    }

    private void setOneBid(double price, int volume) {
        bidDepth.put(price, volume);
    }

    private void setOneAsk(double price, int volume) {
        askDepth.put(price, volume);
    }

}
