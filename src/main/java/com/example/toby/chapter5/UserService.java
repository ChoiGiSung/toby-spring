package com.example.toby.chapter5;

import java.util.List;

public class UserService {
    UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if(canUpgradeLevel(user)){
                upgradeLevel(user);
            }
        }
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level){
            case BASIC:return (user.getLogin() >= 50);
            case SILVER:return (user.getRecommend() >= 30);
            case GOLD:return false;
            default: throw new IllegalArgumentException("알 수 없는 레벨"+level);
        }
    }
}
