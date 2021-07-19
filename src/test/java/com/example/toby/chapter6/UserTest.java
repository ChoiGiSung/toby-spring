package com.example.toby.chapter6;

import com.example.toby.chapter6.Level;
import com.example.toby.chapter6.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    User user;

    @BeforeEach
    void setUp(){
        user = new User("1","name","password",Level.BASIC,30,30,"sam@ple.com");
    }

    @Test
    void upgradeLevel(){
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.nextLevel() == null){
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(level.nextLevel()).isEqualTo(user.getLevel());
        }
    }

    @Test
    void canNotUpgradeLevel(){
        Level[] levels = Level.values();
        assertThrows(IllegalStateException.class,() -> {
            for (Level level : levels) {
                if(level.nextLevel() != null){
                    continue;
                }
                user.setLevel(level);
                user.upgradeLevel();
            }
        });

    }
}
