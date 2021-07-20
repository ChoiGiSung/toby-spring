package com.example.toby.chapter6.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

    Object hello;

    public UppercaseHandler(Object hello) {
        this.hello = hello;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object ret =  method.invoke(hello, args);
        if(ret instanceof String && method.getName().startsWith("say")){
            return ((String)ret).toUpperCase();
        }else {
            return ret;
        }
    }
}
