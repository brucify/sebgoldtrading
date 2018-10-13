package com.bruce.SEBGoldTrading;

import java.util.concurrent.atomic.AtomicLong;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class SebTradingController {

    private final AtomicLong counter = new AtomicLong(); //remove

    @Autowired
    private SebMatchEngine matchEngine;

    @RequestMapping(value = "/depth", method = GET )
    @ApiResponse(code = 200, message = "OK")
    public SebDepthResponse orderBook() {

        return new SebDepthResponse(
                counter.incrementAndGet(),
                matchEngine.getOrderBook()
        );
    }

    @RequestMapping(value = "/order", method = POST )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request parameter")})
    public SebMatchEngine.OrderState order(
            @RequestParam(value="side",     defaultValue="BUY") SebMatchEngine.Side side,
            @RequestParam(value="volume",   defaultValue="0") int volume,
            @RequestParam(value="price",    defaultValue="0.0") double price) throws SebBadRequestException {

        if (volume <= 0 || price <= 0) throw new SebBadRequestException();

        // TODO send order to match engine
        return matchEngine.newOrder(side, price, volume);
    }
}