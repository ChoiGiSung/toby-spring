package com.example.toby.chapter3;

import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class UserDao {

    private ConnectionMaker maker;

    public UserDao(ConnectionMaker maker) {
        this.maker = maker;
    }


    abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;

    public void add(User user) throws SQLException, ClassNotFoundException {
        AddStatement addStatement = new AddStatement(user);
        try (Connection c = maker.makeConnection();
             PreparedStatement ps = addStatement.makePreparedStatement(c)) {
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
        StatementStrategy strategy = new DeleteAllStatement();
        try (Connection c = maker.makeConnection();
             PreparedStatement ps = strategy.makePreparedStatement(c)) {
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

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException, ClassNotFoundException {
        try (Connection c = maker.makeConnection();
             PreparedStatement ps = stmt.makePreparedStatement(c)) {
            ps.execute();
        }
    }

}
