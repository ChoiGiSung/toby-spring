package com.example.toby.v1.chapter6;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionHandler implements InvocationHandler {

    private Object target;
    private PlatformTransactionManager manager;
    private String pattern;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().startsWith(pattern)){
            return invokeTransaction(method,args);//저장된 패턴으로 시작하는 메소드면 트랜젝션 경계 설정
        }
        return method.invoke(target,args);//패턴으로 시작하지 않으면 트랜젝션 설정 x
    }

    private Object invokeTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus status = manager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = method.invoke(target, args);
            manager.commit(status);
            return ret;
        } catch (InvocationTargetException e) {
            manager.rollback(status);
            throw e.getTargetException();
        }
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setManager(PlatformTransactionManager manager) {
        this.manager = manager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
