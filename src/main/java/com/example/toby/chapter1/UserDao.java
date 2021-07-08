package com.example.toby.chapter1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:h2:~/toby","sa",""
        );

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id,name,password) values(?,?,?)");
                ps.setString(1,user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                ps.execute();

                ps.close();
                c.close();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();

        User user =new User();
        user.setId("1");
        user.setName("백기선");
        user.setPassword("married");

        userDao.add(user);
    }
}
