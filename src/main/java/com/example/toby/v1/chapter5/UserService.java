package com.example.toby.v1.chapter5;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_COUNT_FOR_GOLD = 30;

    private PlatformTransactionManager transactionManager;
    UserDao userDao;
    UserLevelUpgradePolicy upgradePolicy;

    public UserService(PlatformTransactionManager transactionManager, UserDao userDao, UserLevelUpgradePolicy upgradePolicy) {
        this.transactionManager = transactionManager;
        this.userDao = userDao;
        this.upgradePolicy = upgradePolicy;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void upgradeLevels() throws Exception {

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw e;
        }

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

    static class TestUserService extends UserService{

        private String id = "4";

        public TestUserService(PlatformTransactionManager transactionManager,UserDao userDao, UserLevelUpgradePolicy upgradePolicy) {
            super(transactionManager,userDao, upgradePolicy);
        }

        @Override
        protected void upgradeLevel(User user) {
            System.out.println(user.getId());
            if(user.getId().equals(id)){
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }

        static class MockMailSender implements MailSender{

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
