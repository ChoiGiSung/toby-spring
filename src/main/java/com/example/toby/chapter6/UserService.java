package com.example.toby.chapter6;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {
    void add(User user);
    void upgradeLevels();
    User get(String id);
    List<User> getAll();
    void update(User user);
    void deleteAll();
}
