package com.crypto.exception;

public class NoResultsFoundException extends Exception {

    public NoResultsFoundException() { super(); }
    public NoResultsFoundException(String message) { super(message); }
    public NoResultsFoundException(String message, Throwable cause) { super(message, cause); }
    public NoResultsFoundException(Throwable cause) { super(cause); }

}
