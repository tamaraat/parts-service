package com.autocare.parts.exception;

public class SparePartNotFoundException
        extends RuntimeException {

    public SparePartNotFoundException(
            String message
    ) {
        super(message);
    }
}