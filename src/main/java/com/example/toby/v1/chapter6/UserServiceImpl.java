package com.example.toby.v1.chapter6;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_COUNT_FOR_GOLD = 30;

    UserDao userDao;
    UserLevelUpgradePolicy upgradePolicy;

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserDao userDao, UserLevelUpgradePolicy upgradePolicy) {
        this.userDao = userDao;
        this.upgradePolicy = upgradePolicy;
    }


    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    public void add(User user) {
        userDao.add(user);
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                System.out.println(user.getName());
                upgradeLevel(user);
            }
        }
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    protected void upgradeLevel(User user) {
        upgradePolicy.upgradeLevel(user);
    }

    // DDD 개념을 적용시키면 일부를 user 도메인으로 옮길 수 있다.
    // 레벨 검증은 아마도 Enum으로?
    //https://stackoverflow.com/questions/2597219/is-it-a-good-idea-to-migrate-business-logic-code-into-our-domain-model
    private boolean canUpgradeLevel(User user) {
        return upgradePolicy.canUpgradeLevel(user);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUpgradePolicy(UserLevelUpgradePolicy upgradePolicy) {
        this.upgradePolicy = upgradePolicy;
    }


    public static class TestUserService extends UserServiceImpl {

        private String id = "4";

        public TestUserService( UserDao userDao, UserLevelUpgradePolicy upgradePolicy) {
            super(userDao, upgradePolicy);
            System.out.println("엥");
        }

        @Override
        protected void upgradeLevel(User user) {
            System.out.println(user.getId());
            if (user.getId().equals(id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }

        @Override
        public List<User> getAll() {
            for (User user : super.getAll()) {
                user.setEmail("ads");
                super.update(user);
            }
            return null;
        }

        public static class MockMailSender implements MailSender {

            private List<String> requests = new ArrayList<>();

            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {
                requests.add(simpleMessage.getTo()[0]);//전송요청을 받은 이메일 주소 0번만 저장
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException {

            }

            public List<String> getRequests() {
                return requests;
            }
        }
    }
}
