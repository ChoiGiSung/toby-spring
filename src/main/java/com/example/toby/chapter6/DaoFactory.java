package com.example.toby.chapter6;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
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
    TxProxyFactoryBean userServiceProxy() throws Exception {
        TxProxyFactoryBean txProxyFactoryBean = new TxProxyFactoryBean();
        txProxyFactoryBean.setManager(platformTransactionManager());
        txProxyFactoryBean.setServiceInterface(UserService.class);
        txProxyFactoryBean.setPattern("upgradeLevels");
        txProxyFactoryBean.setTarget(userService());
        return txProxyFactoryBean;
    }


    @Bean
    UserService userServiceTx(){
        return new UserServiceTx(userService(),platformTransactionManager());
    }

    @Bean
    UserService testUserServiceTx(){
        return new UserServiceTx(testUserService(),platformTransactionManager());
    }

    @Bean
    public UserServiceImpl userService(){
        return new UserServiceImpl(userDao(),userLevelUpgradePolicy());
    }
    @Bean
    public UserServiceImpl.TestUserService testUserService(){
        return new UserServiceImpl.TestUserService(userDao(),userLevelUpgradePolicy());
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
    public MailSender mailSender(){
//        return new DummyMailSender();
        return new UserServiceImpl.TestUserService.MockMailSender();
    }


    @Bean
    public TransactionAdvice transactionAdvice(){
        TransactionAdvice advice = new TransactionAdvice();
        advice.setManager(platformTransactionManager());
        return advice;
    }

    @Bean
    public NameMatchMethodPointcut transactionPointcut(){
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(){
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(transactionAdvice());
        advisor.setPointcut(transactionPointcut());
        return advisor;
    }

    @Bean
    public ProxyFactoryBean userServiceSpringProxy(){
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.addAdvisor(transactionAdvisor());
        proxyFactoryBean.setTarget(userService());
        return proxyFactoryBean;
    }

}
