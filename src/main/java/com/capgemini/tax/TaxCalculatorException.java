package com.capgemini.tax;

public class TaxCalculatorException extends RuntimeException {

    private static final long serialVersionUID = 3009243381236604727L;
    
    public TaxCalculatorException(String message) {
        super("[TaxCalculatorException] " + message);
    }
}
