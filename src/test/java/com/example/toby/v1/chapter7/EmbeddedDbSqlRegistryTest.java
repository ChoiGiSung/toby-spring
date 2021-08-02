package com.example.toby.v1.chapter7;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{
    EmbeddedDatabase embeddedDatabase;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(embeddedDatabase);
        return embeddedDbSqlRegistry;
    }

    @AfterEach
    void tearDown(){
        embeddedDatabase.shutdown();
    }

    @Test
    void transactionalUpdate(){
        checkFindResult("SQL1","SQL2","SQL3");

        Map<String,String> sqlMap = new HashMap<>();
        sqlMap.put("KEY1","수정");
        sqlMap.put("KEㅇㅁㄴY1","수ㅁㄴㅇ정");

        try {
            sqlRegistry.updateSql(sqlMap);
            fail();
        }catch (SqlUpdateFailureException e){

        }

        checkFindResult("SQL1","SQL2","SQL3");

    }
}
