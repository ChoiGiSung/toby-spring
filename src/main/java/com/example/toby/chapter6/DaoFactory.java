package com.example.toby.chapter6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.mail.MailSender;
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
    public UserServiceImpl userService(){
        return new UserServiceImpl(platformTransactionManager(),userDao(),userLevelUpgradePolicy());
    }

    @Bean
    UserLevelUpgradePolicy userLevelUpgradePolicy(){
        UserLevelDefaultPolicy userLevelDefaultPolicy = new UserLevelDefaultPolicy(userDao());
        userLevelDefaultPolicy.setMailSender(mailSender());
        return userLevelDefaultPolicy;
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
    public UserServiceImpl.TestUserService testUserService(){
        return new UserServiceImpl.TestUserService(platformTransactionManager(),userDao(),userLevelUpgradePolicy());
    }

    @Bean
    public MailSender mailSender(){
//        return new DummyMailSender();
        return new UserServiceImpl.TestUserService.MockMailSender();
    }


}
