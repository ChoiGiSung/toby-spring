package com.example.toby.chapter6;

import com.example.toby.chapter6.proxy.Hello;
import com.example.toby.chapter6.proxy.HelloTarget;
import com.example.toby.chapter6.proxy.HelloUppercase;
import com.example.toby.chapter6.proxy.UppercaseHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
