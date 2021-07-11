package com.example.toby.chapter2;

import com.example.toby.chapter2.ConnectionMaker;
import com.example.toby.chapter2.DConnectionMaker;
import com.example.toby.chapter2.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 설정을 담당하는 class
 * */

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao(){
        return new UserDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
