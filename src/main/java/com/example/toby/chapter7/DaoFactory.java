package com.example.toby.chapter7;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 설정을 담당하는 class
 * */

@Configuration
public class DaoFactory {

    @Bean
    public DataSource dataSource(){
        return new SingleConnectionDataSource(
                "jdbc:h2:~/toby","sa","",true
        );
    }

    @Bean
    public MailSender mailSender(){
        return new UserServiceImpl.TestUserService.MockMailSender();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public UserServiceImpl userService(){
        System.out.println("ads");
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
        UserDaoJdbc daoJdbc = new UserDaoJdbc(dataSource());
        daoJdbc.setSqlService(sqlService());
        return daoJdbc;
    }

    @Bean
    public SimpleSqlService sqlService(){
        SimpleSqlService simpleSqlService = new SimpleSqlService();
        Map<String,String> map = new HashMap<>();
        map.put("userAdd","insert into users(id,name,password,level,login,recommend,email) values(?,?,?,?,?,?,?)");
        map.put("userGet","select * from users where id =?");
        map.put("userGetAll","select * from users order by id");
        map.put("userDeleteAll","delete from users");
        map.put("userUpdate","update users set name = ?,password=?,level=?,login=?,recommend = ?, email = ? where id =?");
        map.put("userGetCount","select count(*) from users");
        simpleSqlService.setSqlMap(map);
        return simpleSqlService;
    }

    @Bean
    public SqlService xmlSqlService(){
        XmlSqlService xmlSqlService = new XmlSqlService();
        xmlSqlService.setSqlReader(xmlSqlService);
        xmlSqlService.setSqlRegistry(xmlSqlService);
        return xmlSqlService;
    }



}
