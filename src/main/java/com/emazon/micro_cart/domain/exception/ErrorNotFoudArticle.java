package com.emazon.micro_cart.domain.exception;

public class ErrorNotFoudArticle extends RuntimeException{
    public ErrorNotFoudArticle (String message) {
        super(message);
    }
}
