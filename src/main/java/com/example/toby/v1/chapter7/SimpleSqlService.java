package com.example.toby.v1.chapter7;

import java.util.Map;

public class SimpleSqlService implements SqlService{

    private Map<String,String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);
        if(sql == null){
            throw new SqlRetrievalFailureException(key + "에 대해 못 찾음");
        }else {
            return sql;
        }
    }
}
