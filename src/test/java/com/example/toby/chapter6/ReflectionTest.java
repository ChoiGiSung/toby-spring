package com.example.toby.chapter6;

import com.example.toby.chapter6.proxy.Hello;
import com.example.toby.chapter6.proxy.HelloTarget;
import com.example.toby.chapter6.proxy.HelloUppercase;
import com.example.toby.chapter6.proxy.UppercaseHandler;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    @Test
    void invokeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name = "Spring";

        assertThat(6).isEqualTo(name.length());

        Method lengthMethod = String.class.getMethod("length");
        assertThat(6).isEqualTo(lengthMethod.invoke(name));

        assertThat('S').isEqualTo(name.charAt(0));
        Method charAtMethod = String.class.getMethod("charAt", int.class);

        assertThat('S').isEqualTo(charAtMethod.invoke(name,0));
    }

    @Test
    void simpleProxy(){
        Hello hello = new HelloTarget();
        assertThat("Hello sample").isEqualTo(hello.sayHello("sample"));

        Hello helloUppercase = new HelloUppercase(hello);
        assertThat("HELLO SAMPLE").isEqualTo(helloUppercase.sayHello("sample"));

        Hello proxyInstance = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        assertThat("HELLO SAMPLE").isEqualTo(proxyInstance.sayHello("sample"));
    }

    @Test
    void proxyFactoryBean(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello hello = (Hello) pfBean.getObject();

        assertThat("HELLO SAMPLE").isEqualTo(hello.sayHello("sample"));
    }

    static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    void pointcutAdvisor(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut,new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat("HELLO SAMPLE").isEqualTo(proxiedHello.sayHello("sample"));
    }
}
