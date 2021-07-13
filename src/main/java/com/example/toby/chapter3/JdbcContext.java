package com.example.toby.chapter3;

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
}
