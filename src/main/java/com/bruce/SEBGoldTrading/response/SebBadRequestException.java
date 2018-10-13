package com.bruce.SEBGoldTrading.response;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request parameter")
public class SebBadRequestException extends RuntimeException {

    public SebBadRequestException() { }

}
