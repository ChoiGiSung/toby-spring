package com.example.toby.chapter1;

public class UserDaoTest {

    public static void main(String[] args) {
        // DB 연결 기능 확장 가능
        ConnectionMaker maker = new DConnectionMaker();

        UserDao userDao = new UserDao(maker);
    }
}
