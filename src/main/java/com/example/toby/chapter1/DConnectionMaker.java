package com.example.toby.chapter1;

import java.sql.Connection;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker{
    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        //D 사의 독자적인 코드
        return null;
    }
}
