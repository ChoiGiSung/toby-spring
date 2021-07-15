package com.example.toby.chapter4;

public class DuplicateUserIdException extends RuntimeException{
    public DuplicateUserIdException(Throwable cause) {
        super(cause);
    }
}
