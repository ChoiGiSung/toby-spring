package com.example.toby.v2.chapter1;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ScopeTest {

    @Test
    void 싱글톤_스코프(){
        ApplicationContext context = new AnnotationConfigApplicationContext(
                SingletonBean.class,SingletonClientBean.class
        );
        Set<SingletonBean> beans = new HashSet<>();

        beans.add(context.getBean(SingletonBean.class));
        beans.add(context.getBean(SingletonBean.class));
        beans.add(context.getBean(SingletonClientBean.class).bean1);
        beans.add(context.getBean(SingletonClientBean.class).bean2);

        assertThat(beans.size()).isEqualTo(1);
    }

    static class SingletonBean{}
    static class SingletonClientBean{
        @Autowired SingletonBean bean1;
        @Autowired SingletonBean bean2;
    }

}
