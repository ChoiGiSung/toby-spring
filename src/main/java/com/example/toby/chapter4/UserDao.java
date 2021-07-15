package com.example.toby.chapter4;

import com.example.toby.chapter3.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private JdbcTemplate template;
    private RowMapper<User> userRowMapper =new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {

            User user = new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password"));
            return user;
        }
    };

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
        return template.queryForObject("select * from users where id =?",
                new Object[]{id},userRowMapper
                );
    }

    public List<User> getAll(){
        return this.template.query("select * from users order by id",userRowMapper);
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
        return template.queryForObject("select count(*) from users", Integer.class);
    }


}
