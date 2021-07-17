package com.example.toby.chapter5;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    UserDao userDao;
    List<User> users;

    @BeforeEach
    void setUp(){
        users = Arrays.asList(
                new User("1","name1","password",Level.BASIC,49,0),
                new User("2","name2","password",Level.BASIC,50,0),
                new User("3","name3","password",Level.SILVER,60,29),
                new User("4","name4","password",Level.SILVER,60,30),
                new User("5","name5","password",Level.GOLD,100,100)
        );
        userDao = userService.getUserDao();
    }

    @Test
    public void bean(){
        assertThat(this.userService).isNotNull();
    }

    @Test
    public void upgradeLevels(){
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0),Level.BASIC);
        checkLevel(users.get(1),Level.SILVER);
        checkLevel(users.get(2),Level.SILVER);
        checkLevel(users.get(3),Level.GOLD);
        checkLevel(users.get(4),Level.GOLD);
    }

    private void checkLevel(User user, Level level) {
        User updateUser = userDao.get(user.getId());
        assertThat(updateUser.getLevel()).isEqualTo(level);
    }
}
