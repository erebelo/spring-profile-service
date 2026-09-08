package com.erebelo.springprofileservice.exception.model;

public class ConflictException extends ApiException {

    public ConflictException(String message) {
        super(message);
    }
}
