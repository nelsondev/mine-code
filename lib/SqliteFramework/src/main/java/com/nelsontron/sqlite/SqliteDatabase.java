package com.nelsontron.sqlite;

import java.util.List;

public class SqliteDatabase {

    private String name;
    private String url;

    public SqliteDatabase(String name, String url) {

        if (!name.contains(".db")) name += ".db";

        this.name = name;
        this.url = url + name;
    }

    // getters
    public String getName() { return name; }
    public String getUrl() { return url; }

    // methods
    public void create() {
        SqliteUtil.createDatabase(url);
    }
    public void query(String sql) {
        SqliteUtil.query(url, sql);
    }

    public <T> T select(String sql, Class<T> clazz) {
        return SqliteUtil.select(url, sql, clazz);
    }
    public <T> T select(String sql, T obj) {
        return SqliteUtil.select(url, sql, obj);
    }
    public <T> List<T> selectAll(String sql, Class<T> clazz) {
        return SqliteUtil.selectAll(url, sql, clazz);
    }

    public <T> T get(String table, String id, Class<T> clazz) {
        return SqliteUtil.get(url, table, id, clazz);
    }
    public <T> T getReference(String table, String id, String fieldName, Class<T> clazz) {
        return SqliteUtil.getReference(url, table, id, fieldName, clazz);
    }
    public <T> List<T> getReferences(String table, String id, String fieldName, Class<T> clazz) {
        return SqliteUtil.getReferences(url, table, id, fieldName, clazz);
    }

    public void insert(String table, Object obj) {
        SqliteUtil.insert(url, table, obj);
    }
    public void insert( String table, List<Object> list, Class<?> clazz) {
        SqliteUtil.insert(url, table, list);
    }

    public void updateOrInsert(String table, Object obj) {
        SqliteUtil.updateOrInsert(url, table, obj);
    }
    public void update(String table, Object obj) {
        SqliteUtil.update(url, table, obj);
    }
    public void update(String table, String where, Object obj) {
        SqliteUtil.update(url, table, where, obj);
    }
    public void update(String table, List<Object> list) {
        SqliteUtil.update(url, table, list);
    }

    public void delete(String table, String where) {
        SqliteUtil.delete(url, table, where);
    }
    public void delete(String table, Object obj) {
        SqliteUtil.delete(url, table, obj);
    }
    public void delete(String table, List<Object> list) {
        SqliteUtil.delete(url, table, list);
    }
}
