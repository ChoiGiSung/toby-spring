package com.example.toby.chapter6;

import java.util.List;

public interface UserService {
    void add(User user);
    void upgradeLevels();
    User get(String id);
    List<User> getAll();
    void update(User user);
    void deleteAll();
}
