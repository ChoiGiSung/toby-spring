package com.example.toby;

import com.example.toby.chapter5.DaoFactory;
import com.example.toby.chapter5.Level;
import com.example.toby.chapter5.User;
import com.example.toby.chapter5.UserDaoJdbc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

public class UserDaoTest {

    private ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    private UserDaoJdbc userDao = context.getBean("userDao",UserDaoJdbc.class);

    @BeforeEach
    void deleteAll() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();
        assertThat(0).isEqualTo(userDao.getCount());
    }

    @Test
    void add() throws SQLException, ClassNotFoundException {

        User user1 = user("1");
        User user2 = user("2");
        userDao.add(user1);
        userDao.add(user2);

        User DBUser1 = userDao.get("1");
        User DBUser2 = userDao.get("2");

        assertThat(user1.getId()).isEqualTo(DBUser1.getId());
        assertThat(user2.getId()).isEqualTo(DBUser2.getId());

    }

    @Test
    void count() throws SQLException, ClassNotFoundException {
        User user1 = user("1");
        User user2 = user("2");
        User user3 = user("3");

        userDao.add(user1);
        assertThat(1).isEqualTo(userDao.getCount());

        userDao.add(user2);
        assertThat(2).isEqualTo(userDao.getCount());

        userDao.add(user3);
        assertThat(3).isEqualTo(userDao.getCount());
    }

    @Test
    void getUserFailure() throws SQLException, ClassNotFoundException {
        Assertions.assertThrows(EmptyResultDataAccessException.class,() -> {
            userDao.get("4");
        });

    }

    @Test
    void update(){
        userDao.deleteAll();

        User user = user("1");
        userDao.add(user);

        user.setLevel(Level.GOLD);
        user.setLogin(1000);
        user.setRecommend(999);

        userDao.update(user);

        User updateUser = userDao.get(user.getId());

        checkSameUser(user,updateUser);
    }

    private void checkSameUser(User user, User updateUser) {
        assertThat(user.getId()).isEqualTo(updateUser.getId());
        assertThat(user.getName()).isEqualTo(updateUser.getName());
        assertThat(user.getPassword()).isEqualTo(updateUser.getPassword());
        assertThat(user.getLogin()).isEqualTo(updateUser.getLogin());
        assertThat(user.getRecommend()).isEqualTo(updateUser.getRecommend());
        assertThat(user.getLevel()).isEqualTo(updateUser.getLevel());
    }

    private User user(String id){
        return new User(id,"name","password", Level.BASIC,1,0);
    }

}
