package com.example.toby.chapter1;

/**
 * userDaoTest는 DAO가 어떻게 만들어 지는지 알 수 없다.
 * */
public class UserDaoTest {

    public static void main(String[] args) {
        // DB 연결 기능 확장 가능
        UserDao userDao = new DaoFactory().userDao();
    }
}
