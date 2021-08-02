package com.example.toby.v1.chapter7;

public interface SqlService {

    String getSql(String key) throws SqlRetrievalFailureException;
}
