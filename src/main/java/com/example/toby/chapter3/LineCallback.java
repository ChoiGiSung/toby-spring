package com.example.toby.chapter3;

public interface LineCallback<T> {

    T doSomethingWithLine(String line,T value);
}
