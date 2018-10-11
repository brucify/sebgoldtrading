package com.bruce.SEBGoldTrading;

import java.util.Comparator;
import java.util.TreeMap;

public class SebOrderDepth {

    private final TreeMap<Double, Integer> askDepth = new TreeMap<>();
    private final TreeMap<Double, Integer> bidDepth = new TreeMap<>(Comparator.reverseOrder());

    public SebOrderDepth() {
        // fake order depth to begin with
        bidDepth.put(1300.0, 10);
        bidDepth.put(1299.5, 9);
        bidDepth.put(1299.0, 8);
        bidDepth.put(1298.5, 7);
        bidDepth.put(1298.0, 6);
        bidDepth.put(1297.5, 5);

        // fake order depth to begin with
        askDepth.put(1300.5, 10);
        askDepth.put(1301.0, 9);
        askDepth.put(1301.5, 8);
        askDepth.put(1302.0, 7);
        askDepth.put(1302.5, 6);
        askDepth.put(1303.0, 5);
    }

    public TreeMap<Double, Integer> getAllBids() {
        return bidDepth;
    }

    public TreeMap<Double, Integer> getAllAsks() {
        return askDepth;
    }

    public double getBestBid() {
        if (bidDepth.isEmpty())
            return 0;

        return bidDepth.firstKey();
    }

    public double getBestAsk() {
        if (askDepth.isEmpty())
            return 0;

        return askDepth.firstKey();
    }
}
