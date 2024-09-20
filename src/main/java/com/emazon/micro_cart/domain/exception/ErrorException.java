package com.emazon.micro_cart.domain.exception;

public class ErrorException extends RuntimeException {
    public ErrorException (String message) {
            super(message);
        }

}
