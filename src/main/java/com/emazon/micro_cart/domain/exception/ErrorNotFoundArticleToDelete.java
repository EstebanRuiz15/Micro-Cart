package com.emazon.micro_cart.domain.exception;

public class ErrorNotFoundArticleToDelete extends RuntimeException{
    public ErrorNotFoundArticleToDelete (String message){
        super(message);
    }   
}
