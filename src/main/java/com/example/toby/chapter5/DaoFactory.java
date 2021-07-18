package com.example.toby.chapter5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 설정을 담당하는 class
 * */

@Configuration
public class DaoFactory {


    // 여러 트랜젝션을 지원해 주기 위해 최상위의 트랜젝션 매니저를 사용
    // jpa나 jta는 알맞게 리턴값을 변경하면 됨
    @Bean
    public PlatformTransactionManager platformTransactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public UserService userService(){
        return new UserService(platformTransactionManager(),userDao(),userLevelUpgradePolicy());
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

    @Bean
    public UserService.TestUserService testUserService(){
        return new UserService.TestUserService(platformTransactionManager(),userDao(),userLevelUpgradePolicy());
    }

}
