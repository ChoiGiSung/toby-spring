package com.example.toby.chapter1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {

    private SimpleConnectionMaker maker;

    public UserDao(SimpleConnectionMaker maker) {
        this.maker = maker;
    }

    public void add (User user) throws SQLException, ClassNotFoundException {
        Connection c = maker.makeNewConnection();

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id,name,password) values(?,?,?)");
        ps.setString(1,user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.execute();

        ps.close();
        c.close();
    }

}
