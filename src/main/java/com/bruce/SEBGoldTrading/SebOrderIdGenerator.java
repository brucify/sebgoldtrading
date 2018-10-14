package com.bruce.SEBGoldTrading;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class SebOrderIdGenerator {

    private final AtomicLong counter = new AtomicLong();

    public SebOrderIdGenerator() { }

    public long next() {
        return counter.incrementAndGet();
    }

}
