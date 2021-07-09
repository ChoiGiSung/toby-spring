package com.example.toby.chapter1;

public class DaoFactory {

    public UserDao userDao(){
        return new UserDao(connectionMaker());
    }

    private ConnectionMaker connectionMaker() {
        ConnectionMaker maker = new DConnectionMaker();
        return maker;
    }
}
