package com.capgemini.controller.exception;

public class HTTPClientException extends Exception {

    private static final long serialVersionUID = -6326289490980214975L;

    public HTTPClientException(String message) {
        super(message);
    }
}
