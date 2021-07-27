package com.example.toby.chapter7;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConcurrentHashMapSqlRegistryTest {

    UpdatableSqlRegistry sqlRegistry;

    @BeforeEach
    void setUp() {
        sqlRegistry = new ConcurrentHashMapSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    @Test
    void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    private void checkFindResult(String sql1, String sql2, String sql3) {
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
        checkFindResult("수정1", "SQL2", "수정3");
    }

    @Test
    void updateWithNotExistingKey() {
        assertThrows(SqlUpdateFailureException.class, () -> {
            sqlRegistry.updateSql("없는 키", "SQLwqeewqweq");
        });
    }
}
