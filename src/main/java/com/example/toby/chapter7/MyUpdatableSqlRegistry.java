package com.example.toby.chapter7;

import java.util.Map;

public class MyUpdatableSqlRegistry implements UpdatableSqlRegistry{
    @Override
    public void registerSql(String key, String sql) {

    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        return null;
    }

    @Override
    public void updateSql(String key, String sql) throws SqlRetrievalFailureException {

    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlRetrievalFailureException {

    }
}
