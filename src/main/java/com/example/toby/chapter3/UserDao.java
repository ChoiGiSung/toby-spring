package com.example.toby.chapter3;

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

    public void add(User user) throws SQLException, ClassNotFoundException {
        try (Connection c = maker.makeConnection();
             PreparedStatement ps = c.prepareStatement(
                     "insert into users(id,name,password) values(?,?,?)")) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.execute();

        }

    }

    public User get(String id) throws SQLException, ClassNotFoundException {

        try (Connection c = maker.makeConnection();
             PreparedStatement ps = c.prepareStatement(
                     "select * from users where id =?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                User user = null;
                if (rs.next()) {
                    user = new User(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("password")
                    );
                }
                if (user == null) {
                    throw new EmptyResultDataAccessException(1);
                }

                return user;
            }
        }
    }

    public void deleteAll() throws SQLException, ClassNotFoundException {
        try (Connection c = maker.makeConnection();
             PreparedStatement ps = c.prepareStatement("delete from users")) {
            ps.execute();
        }
    }

    public int getCount() throws SQLException, ClassNotFoundException {

        try (Connection c = maker.makeConnection();
             PreparedStatement ps = c.prepareStatement("select count(*) from users");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            int count = rs.getInt(1);
            return count;
        }

    }

}
