package com.example.toby.v1.chapter6.proxy;

public class HelloTarget implements Hello{

    @Override
    public String sayHello(String name) {
        return "Hello "+name;
    }

    @Override
    public String sayHi(String name) {
        return "Hi "+name;
    }

    @Override
    public String sayThankYou(String name) {
        return "ThankYou "+name;
    }
}
