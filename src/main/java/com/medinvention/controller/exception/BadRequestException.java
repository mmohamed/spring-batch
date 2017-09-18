package com.medinvention.controller.exception;

public class BadRequestException extends HTTPClientException {

    private static final long serialVersionUID = 3772542048226238164L;

    public BadRequestException(String message) {
        super("[BadRequest] " + message);
    }
}
