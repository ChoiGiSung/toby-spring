package com.example.toby.chapter3;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private JdbcTemplate template;

    public UserDao(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public void add(final User user) throws SQLException, ClassNotFoundException {
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("insert into user values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });

    }

    public User get(String id) throws SQLException, ClassNotFoundException {
        return template.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("select * from users where id =?");
                ps.setString(1, id);
                return ps;
            }
        }, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
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
        });
    }

    public void deleteAll() throws SQLException, ClassNotFoundException {
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("delete from users");
            }
        });
    }

    public int getCount() throws SQLException, ClassNotFoundException {
        return template.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                return con.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                int count = rs.getInt(1);
                return count;
            }
        });

    }


}
