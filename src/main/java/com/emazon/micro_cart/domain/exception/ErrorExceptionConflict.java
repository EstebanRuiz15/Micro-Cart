package com.emazon.micro_cart.domain.exception;

public class ErrorExceptionConflict extends RuntimeException {
        public ErrorExceptionConflict (String message) {
                super(message);
            }
}
