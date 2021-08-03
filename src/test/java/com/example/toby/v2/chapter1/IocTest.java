package com.example.toby.v2.chapter1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;

class IocTest {

    String basePath = "static/config/";

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

    @Test
    void DI_정보넣기(){
        StaticApplicationContext context = new StaticApplicationContext();
        context.registerBeanDefinition("printer",new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name","Spring");
        helloDef.getPropertyValues().addPropertyValue("printer",new RuntimeBeanReference("printer"));

        context.registerBeanDefinition("hello",helloDef);

        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString()).isEqualTo("Hello Spring");
    }

    @Test
    void XML_설정으로_컨테이너설정(){
        GenericApplicationContext context = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);

        reader.loadBeanDefinitions(
                "static/HelloXML.xml"
        );
        context.refresh();

        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString()).isEqualTo("Hello Spring");
    }



    @Test
    void ioc_계층구조(){

        ApplicationContext parent = new GenericXmlApplicationContext(basePath + "parentContext.xml");
        GenericApplicationContext child = new GenericApplicationContext(parent);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions(basePath+"childContext.xml");
        child.refresh();

        Printer printer = child.getBean("printer", Printer.class);
        assertThat(printer).isNotNull();
    }
}