package com.izaiasvalentim.general.Common.CustomExceptions;

public class ErrorInProcessServiceException extends RuntimeException {
    public ErrorInProcessServiceException(String message) {
        super(message);
    }
}
