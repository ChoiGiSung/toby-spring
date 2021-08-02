package com.example.toby.v1.chapter7;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedDbTest {
    EmbeddedDatabase database;
    JdbcTemplate template;

    @BeforeEach
    void setUp(){
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        template = new JdbcTemplate(database);
    }

    @AfterEach
    void tearDown(){
        database.shutdown();
    }

    @Test
    void initData(){
        assertThat(template.queryForObject("select count(*) from sqlmap",Integer.class)).isEqualTo(2);

        List<Map<String, Object>> list = template.queryForList("select * from sqlmap order by key_");

        assertThat((String)list.get(0).get("key_")).isEqualTo("KEY1");
        assertThat((String)list.get(0).get("sql_")).isEqualTo("SQL1");
        assertThat((String)list.get(1).get("key_")).isEqualTo("KEY2");
        assertThat((String)list.get(1).get("sql_")).isEqualTo("SQL2");
    }

    @Test
    void insert(){
        template.update("insert into sqlmap(key_,sql_) values (?,?)","KEY3","SQL#");

        assertThat(template.queryForObject("select count(*) from sqlmap",Integer.class)).isEqualTo(3);
    }
}
