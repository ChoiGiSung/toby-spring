package com.example.toby.v2.chapter3;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConfigurableDispatcherServlet extends DispatcherServlet {
    private Class<?>[] classes;
    private String[] locations;

    private ModelAndView modelAndView;

    public ConfigurableDispatcherServlet(String[] locations) {
        this.locations = locations;
    }

    public ConfigurableDispatcherServlet(Class<?>... classes) {
        this.classes = classes;
    }

    public void setLocations(String... locations) {
        this.locations = locations;
    }

    public void serRelativeLocations(Class clazz, String... relativeLocations) {
        String[] locations = new String[relativeLocations.length];
        String currentPath = ClassUtils.classPackageAsResourcePath(clazz) + "/";

        for (int i = 0; i < relativeLocations.length; i++) {
            locations[i] = currentPath + relativeLocations[i];
        }
        this.setLocations(locations);
    }

    public void setClasses(Class<?>... classes) {
        this.classes = classes;
    }

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        modelAndView = null;
        super.service(request, response);
    }

    protected WebApplicationContext createWebApplicationContext() {
        AbstractRefreshableWebApplicationContext context = new AbstractRefreshableWebApplicationContext() {
            @Override
            protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
                if (locations != null) {
                    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
                    reader.loadBeanDefinitions(locations);
                }

                if (classes != null) {
                    AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
                    reader.register(classes);
                }
            }
        };
        context.setServletContext(getServletContext());
        context.setServletConfig(getServletConfig());
        context.refresh();

        return context;
    }

    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.modelAndView = mv;
        super.render(mv,request,response);
    }

    public ModelAndView getModelAndView() {
        return modelAndView;
    }
}
