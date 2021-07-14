package com.example.toby.chapter3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 설정을 담당하는 class
 * */

@Configuration
public class DaoFactory {

    DataSource dataSource;

    public DaoFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDao userDao(){
        return new UserDao(dataSource);
    }

    @Bean
    public JdbcContext jdbcContext(){
        return new JdbcContext(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
