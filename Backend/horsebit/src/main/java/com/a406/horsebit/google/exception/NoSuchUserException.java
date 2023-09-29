package com.a406.horsebit.google.exception;

import com.a406.horsebit.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NoSuchUserException extends CustomException {
    public NoSuchUserException(){

    }

    public NoSuchUserException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}