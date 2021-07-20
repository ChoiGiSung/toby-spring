package com.example.toby.chapter6;

import com.example.toby.chapter6.*;
import com.example.toby.chapter6.mock.MockUserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.example.toby.chapter6.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.toby.chapter6.UserServiceImpl.MIN_RECCOMEND_COUNT_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

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

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(users);

        UserServiceImpl.TestUserService.MockMailSender mockMailSender = mock(UserServiceImpl.TestUserService.MockMailSender.class);
        UserLevelDefaultPolicy defaultPolicy = new UserLevelDefaultPolicy(mockMailSender,mockUserDao);
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(mockUserDao);
        userService.setUpgradePolicy(defaultPolicy);

        userService.upgradeLevels();
        verify(mockUserDao,times(2)).update(any(User.class));

        verify(mockUserDao).update(users.get(1));
        assertThat(Level.SILVER).isEqualTo(users.get(1).getLevel());
        verify(mockUserDao).update(users.get(3));
        assertThat(Level.GOLD).isEqualTo(users.get(3).getLevel());


        ArgumentCaptor<SimpleMailMessage> mailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender,times(2)).send(mailMessageArgumentCaptor.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArgumentCaptor.getAllValues();
        assertThat(users.get(1).getEmail()).isEqualTo(mailMessages.get(0).getTo()[0]);
        assertThat(users.get(3).getEmail()).isEqualTo(mailMessages.get(1).getTo()[0]);
    }

    private void checkUserAndLevel(User user, String expectedId, Level silver) {
        assertThat(expectedId).isEqualTo(user.getId());
        assertThat(silver).isEqualTo(user.getLevel());
    }

    @Test
    void upgradeAllOrNothing() throws Exception {
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(users);

        UserServiceImpl.TestUserService.MockMailSender mockMailSender = mock(UserServiceImpl.TestUserService.MockMailSender.class);
        UserLevelDefaultPolicy defaultPolicy = new UserLevelDefaultPolicy(mockMailSender,mockUserDao);
        UserServiceImpl.TestUserService userService = new UserServiceImpl.TestUserService(mockUserDao,defaultPolicy);
        PlatformTransactionManager manager = mock(PlatformTransactionManager.class);

        UserServiceTx userServiceTx = new UserServiceTx(userService,manager);
        try {
            userServiceTx.upgradeLevels();
            fail("예외 왜 안터짐?");
        }catch (TestUserServiceException e){

        }
        verify(manager,times(1)).rollback(any());
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
