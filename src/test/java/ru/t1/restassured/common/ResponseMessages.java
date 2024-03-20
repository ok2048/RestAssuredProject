package ru.t1.restassured.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseMessages {
    public static final String MISSING_AUTHORIZATION_HEADER = "Missing Authorization Header";
    public static final String BAD_AUTHORIZATION_HEADER = "Bad Authorization header. Expected 'Authorization: Bearer <JWT>'";
    public static final String BAD_REQUEST = "Bad request";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String PRODUCT_NOT_FOUND_IN_CART = "Product not found in cart";
}
