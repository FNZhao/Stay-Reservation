package com.zhaofn.staybooking.model;

public class Token {//这个可以写成record，因为token不存到db里
    private final String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
