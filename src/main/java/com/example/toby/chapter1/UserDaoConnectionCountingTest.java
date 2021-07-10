package com.example.toby.chapter1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

/**
 * userDaoTest는 DAO가 어떻게 만들어 지는지 알 수 없다.
 * */
public class UserDaoConnectionCountingTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //설정 정보를 이용하는 어플리케이션 컨택스트 (빈 팩토리,ioc 컨테이너) ->설정 정보를 토태로 빈의 생성과 관계를 설정한다.
        //실질적인 생성과 관계는 Configuration이 함
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);

        UserDao userDao = context.getBean("userDao",UserDao.class);

        userDao.add(new User("2","nae","123"));

        CountingConnectionMaker connectionMaker = context.getBean("connectionMaker", CountingConnectionMaker.class);

        System.out.println(connectionMaker.getCounter());
    }
}
