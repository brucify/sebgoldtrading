package com.bruce.SEBGoldTrading;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such order")
class SebHttpResponse {

    public SebHttpResponse() {

    }

}
