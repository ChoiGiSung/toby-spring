package com.example.toby.chapter5;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class UserService {

    public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_COUNT_FOR_GOLD = 30;

    UserDao userDao;
    UserLevelUpgradePolicy upgradePolicy;
    DataSource dataSource;

    public UserService(UserDao userDao, UserLevelUpgradePolicy upgradePolicy) {
        this.userDao = userDao;
        this.upgradePolicy = upgradePolicy;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void upgradeLevels() throws Exception {
        System.out.println(dataSource);
        TransactionSynchronizationManager.initSynchronization();
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            c.commit();
        }catch (Exception e){
            c.rollback();
            throw e;
        }finally {
            DataSourceUtils.releaseConnection(c,dataSource);
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }

    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
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

        public TestUserService(UserDao userDao, UserLevelUpgradePolicy upgradePolicy) {
            super(userDao, upgradePolicy);
        }

        @Override
        protected void upgradeLevel(User user) {
            System.out.println(user.getId());
            if(user.getId().equals(id)){
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }
}
