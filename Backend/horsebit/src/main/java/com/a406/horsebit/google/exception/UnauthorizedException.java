package com.a406.horsebit.google.exception;

import com.a406.horsebit.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(){

    }

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}