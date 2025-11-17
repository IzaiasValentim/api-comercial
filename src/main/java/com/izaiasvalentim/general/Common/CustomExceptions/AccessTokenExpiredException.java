package com.izaiasvalentim.general.Common.CustomExceptions;

public class AccessTokenExpiredException extends RuntimeException {
    public AccessTokenExpiredException(String message) {
        super(message);
    }
}
