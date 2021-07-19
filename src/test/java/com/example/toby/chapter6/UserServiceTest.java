package com.example.toby.chapter6;

import com.example.toby.chapter6.*;
import com.example.toby.chapter6.mock.MockUserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.example.toby.chapter6.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.toby.chapter6.UserServiceImpl.MIN_RECCOMEND_COUNT_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userServiceTx;

    @Autowired
    DataSource dataSource;

    @Autowired
    UserService testUserServiceTx;

    @Autowired
    ApplicationContext context;

    @Autowired
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
    }

    @Test
    void bean() {
        assertThat(this.userServiceTx).isNotNull();
    }

    @Test
    void upgradeLevels() throws Exception {

        MockUserDao mockUserDao = new MockUserDao(users);
        UserServiceImpl.TestUserService.MockMailSender mailSender = new UserServiceImpl.TestUserService.MockMailSender();
        UserLevelDefaultPolicy defaultPolicy = new UserLevelDefaultPolicy(mailSender,mockUserDao);
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(mockUserDao);
        userService.setUpgradePolicy(defaultPolicy);

        userService.upgradeLevels();
        List<User> updated = mockUserDao.getUpdated();
        assertThat(2).isEqualTo(updated.size());
        checkUserAndLevel(updated.get(0),"2",Level.SILVER);
        checkUserAndLevel(updated.get(1),"4",Level.GOLD);

        List<String> requests = mailSender.getRequests();
        assertThat(2).isEqualTo(requests.size());
        assertThat(users.get(1).getEmail()).isEqualTo(requests.get(0));
        assertThat(users.get(3).getEmail()).isEqualTo(requests.get(1));
    }

    private void checkUserAndLevel(User user, String expectedId, Level silver) {
        assertThat(expectedId).isEqualTo(user.getId());
        assertThat(silver).isEqualTo(user.getLevel());
    }

    @Test
    void upgradeAllOrNothing() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserServiceTx.upgradeLevels();
            fail("예외 왜 안터짐?");
        }catch (TestUserServiceException e){

        }
        checkLevelUpgraded(users.get(1),false);
    }

    @Test
    @DirtiesContext
    void upgradeLevelsDummyMailSender() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        UserServiceImpl.TestUserService.MockMailSender mailSender = context.getBean("mailSender", UserServiceImpl.TestUserService.MockMailSender.class);
        //DI로 인해 set이 불가능 , 빈 생성시 변경해줬다.

        userServiceTx.upgradeLevels();

        checkLevelUpgraded(users.get(0),false);
        checkLevelUpgraded(users.get(1),true);
        checkLevelUpgraded(users.get(2),false);
        checkLevelUpgraded(users.get(3),true);
        checkLevelUpgraded(users.get(4),false);

        List<String> request = mailSender.getRequests();
        assertThat(2).isEqualTo(request.size());
        assertThat(users.get(1).getEmail()).isEqualTo(request.get(0));
        assertThat(users.get(3).getEmail()).isEqualTo(request.get(1));
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
