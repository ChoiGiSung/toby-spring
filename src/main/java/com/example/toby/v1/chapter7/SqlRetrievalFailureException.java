package com.example.toby.v1.chapter7;

public class SqlRetrievalFailureException extends RuntimeException {

    public SqlRetrievalFailureException(String message) {
        super(message);
    }

    public SqlRetrievalFailureException(Throwable cause) {
        super(cause);
    }

    public SqlRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
