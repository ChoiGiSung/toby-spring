package com.example.toby.v1.chapter7;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry{

    private JdbcTemplate template;
    private TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.transactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(dataSource)
        );
    }

    @Override
    public void registerSql(String key, String sql) {
        template.update("insert into sqlmap(key_,sql_) values (?,?)",key,sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try{
            return template.queryForObject("select sql_ from sqlmap where key_ = ?",String.class,key);
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
    public void updateSql(final Map<String, String> sqlmap) throws SqlUpdateFailureException {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
                    updateSql(entry.getKey(),entry.getValue());
                }
            }
        });

    }
}
