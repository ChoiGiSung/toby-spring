package com.example.toby.chapter7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractUpdatableSqlRegistryTest {

    UpdatableSqlRegistry sqlRegistry;

    @BeforeEach
    void setUp(){
        sqlRegistry = createUpdatableSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();


    protected void checkFindResult(String sql1, String sql2, String sql3) {
        assertThat(sql1).isEqualTo(sqlRegistry.findSql("KEY1"));
        assertThat(sql2).isEqualTo(sqlRegistry.findSql("KEY2"));
        assertThat(sql3).isEqualTo(sqlRegistry.findSql("KEY3"));
    }

    @Test
    void unknownKey() {
        assertThrows(SqlNotFoundException.class, () -> {
            sqlRegistry.findSql("SQLwqeewqweq");
        });
    }

    @Test
    void updateSingle() {
        sqlRegistry.updateSql("KEY2", "수정이요");
        checkFindResult("SQL1", "수정이요", "SQL3");
    }

    @Test
    void updateMulti() {
        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "수정1");
        sqlmap.put("KEY2", "수정2");

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("수정1", "수정2", "SQL3");
    }

    @Test
    void updateWithNotExistingKey() {
        assertThrows(SqlUpdateFailureException.class, () -> {
            sqlRegistry.updateSql("없는 키", "SQLwqeewqweq");
        });
    }
}
