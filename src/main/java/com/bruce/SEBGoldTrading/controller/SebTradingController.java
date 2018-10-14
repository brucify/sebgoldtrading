package com.bruce.SEBGoldTrading.controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.bruce.SEBGoldTrading.SebOrder;
import com.bruce.SEBGoldTrading.SebOrderDispatcher;
import com.bruce.SEBGoldTrading.SebOrderMatchEngine;
import com.bruce.SEBGoldTrading.response.SebBadRequestException;
import com.bruce.SEBGoldTrading.response.SebDepthResponse;
import com.bruce.SEBGoldTrading.response.SebTradeResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class SebTradingController {

    @Autowired
    private SebOrderMatchEngine matchEngine;

    @Autowired
    private SebOrderDispatcher orderDispatcher;

    @RequestMapping(value = "/depth", method = GET )
    @ApiResponse(code = 200, message = "OK")
    public SebDepthResponse orderDepth() {

        return new SebDepthResponse(
                matchEngine.getOrderBook()
        );
    }

    @RequestMapping(value = "/orders", method = GET )
    @ApiResponse(code = 200, message = "OK")
    public ConcurrentHashMap<Long, SebOrder> orders() {
        return matchEngine.getOrders();
    }

    @RequestMapping(value = "/orders/{order_id}", method = GET )
    @ApiResponse(code = 200, message = "OK")
    public SebOrder orders(@PathVariable("order_id") String order_id) {
        return matchEngine.getOrders().get((Long.parseLong(order_id)));
    }

    @RequestMapping(value = "/orders", method = POST )
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad request parameter")
    })
    public SebOrder orders(
            @RequestParam(value="side",     defaultValue="BUY") SebOrder.Side side,
            @RequestParam(value="volume",   defaultValue="0") int volume,
            @RequestParam(value="price",    defaultValue="0.0") double price
    ) throws SebBadRequestException {

        if (volume <= 0 || price <= 0) throw new SebBadRequestException();

        // TODO send order to match engine
        return orderDispatcher.newOrder(side, price, volume);
    }



    @RequestMapping(value = "/trades", method = GET )
    @ApiResponse(code = 200, message = "OK")
    public SebTradeResponse trades() {

        return new SebTradeResponse(
                //matchEngine.getTrades()
        );
    }
}