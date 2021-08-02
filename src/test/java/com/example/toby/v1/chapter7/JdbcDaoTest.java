package com.example.toby.v1.chapter7;
import com.example.toby.v1.chapter7.Level;
import com.example.toby.v1.chapter7.User;
import com.example.toby.v1.chapter7.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.example.toby.v1.chapter7.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.toby.v1.chapter7.UserServiceImpl.MIN_RECCOMEND_COUNT_FOR_GOLD;

@SpringBootTest
public class JdbcDaoTest {

    List<User> users;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("1", "name1", "password", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0, "sample@sda.com"),
                new User("2", "name2", "password", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0, "sample@sda.com"),
                new User("3", "name3", "password", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD - 1, "sample@sda.com"),
                new User("4", "name4", "password", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD, "sample@sda.com"),
                new User("5", "name5", "password", Level.GOLD, 100, Integer.MAX_VALUE, "sample@sda.com")
        );
    }


    @Test
    void 유저_삽입(){
        userService.deleteAll();

        for (User user : users) {
            userService.add(user);
        }

        List<User> users = userService.getAll();
        for (User user : users) {
            System.out.println(user.getId());
        }

    }


}
