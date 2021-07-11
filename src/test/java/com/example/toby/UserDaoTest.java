package com.example.toby;

import com.example.toby.chapter2.DaoFactory;
import com.example.toby.chapter2.User;
import com.example.toby.chapter2.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

public class UserDaoTest {

    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    UserDao userDao = context.getBean("userDao",UserDao.class);

    @BeforeEach
    void deleteAll() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();
        assertThat(0).isEqualTo(userDao.getCount());
    }

    @Test
    void add() throws SQLException, ClassNotFoundException {

        User user = user();
        userDao.add(user);

        User DBUser = userDao.get("1");

        assertThat(user.getId()).isEqualTo(DBUser.getId());

    }

    User user(){
        return new User("1","name","password");
    }

}
