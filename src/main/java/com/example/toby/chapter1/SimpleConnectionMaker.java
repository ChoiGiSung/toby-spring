package com.example.toby.chapter1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {

    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:h2:~/toby","sa",""
        );
        return c;
    }
}
