package com.example.toby.chapter7;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry{

    private JdbcTemplate template;

    public void setDataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public void registerSql(String key, String sql) {
        template.update("insert into sqlmap(key_,sql_) values (?,?)",key,sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try{
            return template.queryForObject("select sql_ from sqlmap where key_ = ?",String.class);
        }catch (EmptyResultDataAccessException e){
            throw new SqlNotFoundException(key + "에 해당하는 SQL을 찾을 수 없습니다",e);
        }
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int affected = template.update("update sqlmap set sql_ = ? where key_ = ?", sql, key);

        if(affected == 0){
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다");
        }
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
            updateSql(entry.getKey(),entry.getValue());
        }
    }
}
