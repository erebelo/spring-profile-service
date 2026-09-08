package com.erebelo.springprofileservice.exception.model;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(message);
    }
}
