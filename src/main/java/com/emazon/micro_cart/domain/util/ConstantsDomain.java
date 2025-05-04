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
    public static final Integer ZERO=0;
    public static final String NO_ARTICLES_FOUND_EXCEPTION_MESSAGE="NO articles were found for this page";
    public static final String TOTAL_PAGES=", total pages is: ";
    public static final String DESC="desc";
    public static final String CATEGORIES="categories";
    public static final String BRAND="brand";
    public static final String NO_FOUND_CART="You don't have a car yet";
    public static final String NO_ARTICLES_IN_CART="No items have been added to the cart yet";
    public static final Integer ONE=1;
    public static final Integer SEVEN=7;
    public static final String NOTHING_THAT_BUY="You still don't have anything to buy";
}
