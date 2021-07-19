package com.example.toby.chapter6;


import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import static com.example.toby.chapter6.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.toby.chapter6.UserService.MIN_RECCOMEND_COUNT_FOR_GOLD;

public class UserLevelDefaultPolicy implements UserLevelUpgradePolicy {

    private MailSender mailSender;
    UserDao userDao;

    public UserLevelDefaultPolicy(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용잔미의 등급이"+user.getLevel().name());

        this.mailSender.send(mailMessage);
    }

    // DDD 개념을 적용시키면 일부를 user 도메인으로 옮길 수 있다.
    // 레벨 검증은 아마도 Enum으로?
    //https://stackoverflow.com/questions/2597219/is-it-a-good-idea-to-migrate-business-logic-code-into-our-domain-model
    public boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level){
            case BASIC:return (user.getLogin() >= MIN_LOG_COUNT_FOR_SILVER);
            case SILVER:return (user.getRecommend() >= MIN_RECCOMEND_COUNT_FOR_GOLD);
            case GOLD:return false;
            default: throw new IllegalArgumentException("알 수 없는 레벨"+level);
        }
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
