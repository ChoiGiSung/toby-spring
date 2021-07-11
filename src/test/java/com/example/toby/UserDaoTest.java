package com.example.toby;

import com.example.toby.chapter1.DaoFactory;
import com.example.toby.chapter1.User;
import com.example.toby.chapter1.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {

    @Test
    void add() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao",UserDao.class);

        User user = user();
        userDao.add(user);

        User DBUser = userDao.get("3");

        Assertions.assertThat(user.getId()).isEqualTo(DBUser.getId());

    }

    User user(){
        return new User("3","name","password");
    }

}
