package com.example.toby.chapter7;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService,SqlRegistry,SqlReader{

    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;
    private Map<String,String> sqlMap = new HashMap<>();
    private String sqlMapFile;

    @PostConstruct
    public void loadSql(){
        this.sqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        }catch (SqlNotFoundException e){
            throw new SqlRetrievalFailureException(e);
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key,sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        return null;
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        //리지스트리에 등록
    }

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }


    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }
}
