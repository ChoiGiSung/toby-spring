package com.example.toby.v1.chapter3;

public interface LineCallback<T> {

    T doSomethingWithLine(String line,T value);
}
