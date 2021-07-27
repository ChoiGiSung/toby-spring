package com.example.toby.chapter7;

import org.springframework.jdbc.object.SqlUpdate;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry{

    void updateSql(String key,String sql) throws SqlUpdateFailureException;
    void updateSql(Map<String,String> sqlmap) throws SqlUpdateFailureException;

}
