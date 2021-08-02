package com.example.toby.v2.chapter1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
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

    @Test
    void BeanDefinition을_이용한_빈등록(){
        StaticApplicationContext context = new StaticApplicationContext();

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name","Spring");

        context.registerBeanDefinition("hello2",helloDef);
        context.registerSingleton("hello1",Hello.class);

        Hello hello1 = context.getBean("hello1", Hello.class);
        Hello hello2 = context.getBean("hello2", Hello.class);

        assertThat(hello2.sayHello()).isEqualTo("Hello Spring");
        assertThat(hello1).isNotEqualTo(hello2);

        assertThat(context.getBeanFactory().getBeanDefinitionCount()).isEqualTo(2);

    }
}