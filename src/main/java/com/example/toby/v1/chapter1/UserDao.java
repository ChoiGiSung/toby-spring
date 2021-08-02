package com.example.toby.v1.chapter1;

import java.sql.*;

public class UserDao {

    private ConnectionMaker maker;

    public UserDao(ConnectionMaker maker) {
        this.maker = maker;
    }

    public void add (User user) throws SQLException, ClassNotFoundException {
        Connection c = maker.makeConnection();

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id,name,password) values(?,?,?)");
        ps.setString(1,user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.execute();

        ps.close();
        c.close();
    }

    public User get(String id) throws SQLException, ClassNotFoundException {
        Connection c = maker.makeConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id =?"
        );
        ps.setString(1,id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password")
        );

        rs.close();
        ps.close();
        c.close();

        return user;
    }

}
