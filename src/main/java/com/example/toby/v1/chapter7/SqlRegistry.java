package com.example.toby.v1.chapter7;

public interface SqlRegistry {

    void registerSql(String key,String sql);
    String findSql(String key) throws SqlNotFoundException;
}
