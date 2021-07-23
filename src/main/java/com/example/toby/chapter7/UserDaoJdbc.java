package com.example.toby.chapter7;

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

    private String sqlAdd;
    private JdbcTemplate template;
    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {

            User user = new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    Level.valueOf(rs.getInt("level")),
                    rs.getInt("login"),
                    rs.getInt("recommend"),
                    rs.getString("email")

            );
            return user;
        }
    };

    public UserDaoJdbc(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(final User user) {
        template.update(
                sqlAdd,user.getId(),user.getName(),user.getPassword(),user.getLevel().getValue(),
                        user.getLogin(),user.getRecommend(),user.getEmail());

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

    public void update(User user){
        this.template.update(
                "update users set name = ?,password=?,level=?,login=?, " +
                        "recommend = ?, email = ? where id =?",user.getName(),user.getPassword(),
                user.getLevel().getValue(),user.getLogin(),user.getRecommend(),user.getEmail(),user.getId()
        );
    }

    public int getCount() {
        return template.queryForObject("select count(*) from users", Integer.class);
    }

    public void setSqlAdd(String sqlAdd) {
        this.sqlAdd = sqlAdd;
    }
}
