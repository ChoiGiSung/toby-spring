package com.example.toby.chapter5;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.example.toby.chapter5.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.toby.chapter5.UserService.MIN_RECCOMEND_COUNT_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.InstanceOfAssertFactories.PATH;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    DataSource dataSource;

    @Autowired
    UserService.TestUserService testUserService;

    UserDao userDao;
    List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("1", "name1", "password", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER-1, 0,"sample@sda.com"),
                new User("2", "name2", "password", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0,"sample@sda.com"),
                new User("3", "name3", "password", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD-1,"sample@sda.com"),
                new User("4", "name4", "password", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD,"sample@sda.com"),
                new User("5", "name5", "password", Level.GOLD, 100, Integer.MAX_VALUE,"sample@sda.com")
        );
        userDao = userService.getUserDao();
    }

    @Test
    void bean() {
        assertThat(this.userService).isNotNull();
    }

    @Test
    void upgradeLevels() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    void upgradeAllOrNothing() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("예외 왜 안터짐?");
        }catch (TestUserServiceException e){

        }
        checkLevelUpgraded(users.get(1),false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updateUser = userDao.get(user.getId());
        if(upgraded){
            assertThat(user.getLevel().nextLevel()).isEqualTo(updateUser.getLevel());
        }else {
            assertThat(user.getLevel()).isEqualTo(updateUser.getLevel());
        }
    }
}
