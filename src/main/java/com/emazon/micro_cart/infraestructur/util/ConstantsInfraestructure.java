package com.emazon.micro_cart.infraestructur.util;

public final  class ConstantsInfraestructure {
    
    private ConstantsInfraestructure(){
        throw new IllegalStateException("Utility class");
    }

    public static final String AUTHORIZATION="Authorization";
    public static final String BEARER="Bearer ";
    public static final String ACCES_DENIED_MESSAGE="{\\\"error\\\": \\\"Access Denied: ";
    public static final String KEY="294A404E635266556A586E327235753878214125442A472D4B6150645367566B";
    public static final String ADD_WITH_EXIT="article added with exit";
    public static final String ID="id";
    public static final String QUANTITY="quantity";
    public static final String DELETE_WHIT_SUCESS="delete with exit";
    public static final String ASEND="asc";
    public static final String TEN_STRING="10";
    public static final String ZERO_STRING="0";
}
