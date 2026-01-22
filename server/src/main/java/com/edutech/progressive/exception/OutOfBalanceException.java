package com.edutech.progressive.exception;
public class OutOfBalanceException extends RuntimeException {
    public OutOfBalanceException(String message) {
        super(message);
    }
}