package com.example.toby.v2.chapter3;

import org.springframework.stereotype.Component;

@Component
public class HelloSpring {

    public String SayHello(String name){
        return "Hello" + name;
    }
}
