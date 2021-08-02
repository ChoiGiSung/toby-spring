package com.example.toby.v1.chapter6.mock;

import com.example.toby.v1.chapter6.User;
import com.example.toby.v1.chapter6.UserDao;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao {

    private List<User> users;
    private List<User> updated = new ArrayList<>();

    public MockUserDao(List<User> users) {
        this.users = users;
    }

    @Override
    public List<User> getAll() {
        return this.users;
    }

    @Override
    public void update(User user) {
        updated.add(user);
    }

    public List<User> getUpdated() {
        return updated;
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }
    @Override
    public void add(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User get(String id) {
        throw new UnsupportedOperationException();
    }
}
