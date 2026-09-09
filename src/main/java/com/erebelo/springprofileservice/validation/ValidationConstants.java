package com.erebelo.springprofileservice.validation;

public final class ValidationConstants {

    private ValidationConstants() {
        // Prevents instantiation
    }

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

}
