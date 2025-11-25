package com.izaiasvalentim.general.Common.CustomExceptions;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String message) {
        super(message);
    }
}
