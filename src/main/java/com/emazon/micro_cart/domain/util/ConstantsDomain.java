package com.emazon.micro_cart.domain.util;

public final class  ConstantsDomain {
    private ConstantsDomain(){
        throw new IllegalStateException("Utility class");
    }
    
    public static final String ERROR_VALIDATION="";
    public static final String ERROR_3_CATEGORIES_REPEAT="There cannot be more than 3 articles with the same category";
    public static final String MESSAGE_ONLY_HAVE="at this moment only have ";
    public static final String PRODUCTS=" products";
    public static final String MESSAGE_DATE_ARRIVE_SUPPLY="in 7 days arrive supply to this ";
    public static final String NOT_FOUND_ARTICLE="not found article: ";
    public static final String COMUNICATION_ERROR_WITH_SERVICE="Error communicating with stock service ";
    public static final String ADD_WITH_EXIT="article added with exit";
    public static final String DELETE_WHIT_SUCESS="delete with exit";
    public static final String NOT_ARTICLE_TO_DELETE="The article has already been deleted";
}
