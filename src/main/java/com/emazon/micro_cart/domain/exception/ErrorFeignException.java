package com.emazon.micro_cart.domain.exception;

public class ErrorFeignException extends RuntimeException {
    public ErrorFeignException(String message){
        super (message);
    }
}
