package com.example.toby.chapter6;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.AbstractExpressionPointcut;
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
//
//    @Bean
//    UserService testUserServiceTx(){
//        return new UserServiceTx(testUserService(),platformTransactionManager());
//    }

    @Bean
    public UserServiceImpl userService(){
        return new UserServiceImpl(userDao(),userLevelUpgradePolicy());
    }

    @Bean
    public UserServiceImpl testUserService(){
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

//    @Bean
//    public NameMatchMethodPointcut transactionPointcut(){
//        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
//        pointcut.setMappedName("upgrade*");
//        return pointcut;
//    }

//    @Bean
//    public DefaultPointcutAdvisor transactionAdvisor(){
//        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
//        advisor.setAdvice(transactionAdvice());
//        advisor.setPointcut(aspectJExpressionPointcut());
//        return advisor;
//    }
//
//    @Bean
//    public ProxyFactoryBean userServiceSpringProxy(){
//        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
//        proxyFactoryBean.setTarget(userService());
//        proxyFactoryBean.addAdvisor(transactionAdvisor());
//        return proxyFactoryBean;
//    }

    //빈 후처리기
//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        return new DefaultAdvisorAutoProxyCreator();
//    }

    //포인트 컷
    @Bean
    public NameMatchClassMethodPointCut defaultAdvisorTransactionPointcut(){
        NameMatchClassMethodPointCut pointCut = new NameMatchClassMethodPointCut();
        pointCut.setMappedClassName("*Service");
        pointCut.setMappedName("upgrade*");
        return pointCut;
    }

//     포인트 컷 표현식으로 기존 포인트 컷을 대체 -> 기존 포인트 컷은 매번 구현체를 만들어야 했다
    @Bean
    public AspectJExpressionPointcut aspectJExpressionPointcut(){
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("bean(*UserService)");
        return pointcut;
    }

}
