package com.cleverdeath.cryptolabsecond.exception;

public class SDESServiceException extends Exception {
    public SDESServiceException() {
    }

    public SDESServiceException(String message) {
        super(message);
    }

    public SDESServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDESServiceException(Throwable cause) {
        super(cause);
    }
}
