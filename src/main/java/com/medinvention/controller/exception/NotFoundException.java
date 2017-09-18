package com.medinvention.controller.exception;

public class NotFoundException extends HTTPClientException {

    private static final long serialVersionUID = 2029981641599440731L;

    public NotFoundException(String message) {
        super("[NotFound] " + message);
    }
}
