package com.example.toby.v1.chapter3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {

    private ConnectionMaker maker;

    public JdbcContext(ConnectionMaker maker) {
        this.maker = maker;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException, ClassNotFoundException {
        try (Connection c = maker.makeConnection();
             PreparedStatement ps = stmt.makePreparedStatement(c)) {
            ps.execute();
        }
    }

    public void executeSql(final String query) throws SQLException, ClassNotFoundException {
        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(query);
            }
        });
    }

    public ConnectionMaker getMaker() {
        return maker;
    }
}
