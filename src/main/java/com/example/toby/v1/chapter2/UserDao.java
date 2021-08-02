package com.example.toby.v1.chapter2;

import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        User user = null;
        if(rs.next()){
            user = new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password")
            );
        }


        rs.close();
        ps.close();
        c.close();

        if(user == null){
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }

    public void deleteAll() throws SQLException, ClassNotFoundException {
        Connection c = maker.makeConnection();

        PreparedStatement ps = c.prepareStatement("delete from users");

        ps.execute();

        ps.close();
        c.close();
    }

    public int getCount() throws SQLException, ClassNotFoundException {
        Connection c = maker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        c.close();
        ps.close();

        return count;
    }

}
