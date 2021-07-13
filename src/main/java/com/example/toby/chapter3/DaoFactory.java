package com.example.toby.chapter3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 설정을 담당하는 class
 * */

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao(){
        return new UserDaoDeleteAll(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
