package com.example.toby.chapter5;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

/**
 * 설정을 담당하는 class
 * */

@Configuration
public class DaoFactory {

    @Bean
    public UserService userService(){
        return new UserService(userDao(),userLevelUpgradePolicy());
    }

    @Bean UserLevelUpgradePolicy userLevelUpgradePolicy(){
        return new UserLevelDefaultPolicy(userDao());
    }

    @Bean
    public UserDao userDao(){
        return new UserDaoJdbc(dataSource());
    }

    @Bean
    public DataSource dataSource(){
        return new SingleConnectionDataSource(
                "jdbc:h2:~/toby","sa","",true
        );
    }

}
