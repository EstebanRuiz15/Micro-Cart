package com.emazon.micro_cart.domain.exception;

public class ErrorExceptionArticleEmpty extends RuntimeException {
    public ErrorExceptionArticleEmpty (String message) {
        super(message);
    }
}
