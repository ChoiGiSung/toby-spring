package com.example.toby.v2.chapter1;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class IocTest {

    @Test
    void ioc_빈등록(){
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerSingleton("hello1",Hello.class);

        Hello hello1 = context.getBean("hello1", Hello.class);
        assertThat(hello1).isNotNull();
    }
}