package com.example.toby.v1.chapter4;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate template;
    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {

            User user = new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password"));
            return user;
        }
    };

    public UserDaoJdbc(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(final User user) {
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

    public User get(String id) {
        return template.queryForObject("select * from users where id =?",
                new Object[]{id}, userRowMapper
        );
    }

    public List<User> getAll() {
        return this.template.query("select * from users order by id", userRowMapper);
    }

    public void deleteAll() {
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("delete from users");
            }
        });
    }

    public int getCount() {
        return template.queryForObject("select count(*) from users", Integer.class);
    }


}
