package com.example.toby.chapter1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NUserDao extends UserDao{

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:h2:~/toby","sa",""
        );
        return c;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        NUserDao userDao = new NUserDao();

        User user =new User();
        user.setId("1");
        user.setName("백기선");
        user.setPassword("married");

        userDao.add(user);
    }
}
