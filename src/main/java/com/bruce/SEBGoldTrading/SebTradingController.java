package com.bruce.SEBGoldTrading;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class SebTradingController {

//    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final SebOrderMatchEngine orderMatchEngine = new SebOrderMatchEngine();

    @RequestMapping(value = "/depth", method = GET )
    public SebOrderDepthResponse orderDepth() {

        return new SebOrderDepthResponse(
                counter.incrementAndGet(),
                orderMatchEngine.getOrderDepth()
        );
    }

    @RequestMapping(value = "/order", method = POST )
    public SebOrderMatchEngine.OrderState order(
            @RequestParam(value="side",     defaultValue="BUY") SebOrderMatchEngine.Side side,
            @RequestParam(value="volume",   defaultValue="0") int volume,
            @RequestParam(value="price",    defaultValue="0.0") double price) {

        // TODO send order to match engine
        return orderMatchEngine.insertOrder(side, price, volume);
    }
}
