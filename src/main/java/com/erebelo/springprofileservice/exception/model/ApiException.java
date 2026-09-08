package com.erebelo.springprofileservice.exception.model;

public abstract class ApiException extends RuntimeException {

    protected ApiException(String message) {
        super(message);
    }
}
