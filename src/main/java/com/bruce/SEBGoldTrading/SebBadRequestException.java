package com.bruce.SEBGoldTrading;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request parameter")
class SebBadRequestException extends RuntimeException {

    public SebBadRequestException() {

    }

}
