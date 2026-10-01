package com.example.demo.exception;

public class LoanAlreadyReturnedException extends RuntimeException {

    public LoanAlreadyReturnedException(String message) {
        super(message);
    }
    
}
