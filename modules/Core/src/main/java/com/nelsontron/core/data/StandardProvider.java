package com.nelsontron.core.data;

import com.nelsontron.sqlite.SqliteEngine;
import com.nelsontron.sqlite.SqliteUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class StandardProvider<T> implements IStandardProvider<T> {
    private final SqliteEngine engine;
    private final List<T> list;
    private final String table;

    public StandardProvider(SqliteEngine engine, String table) {
        this.engine = engine;
        this.table = table;
        list = new ArrayList<>();
    }

    // getters
    @Override
    public void init(Object value) { }
    public List<T> getList() {
        return list;
    }
    protected SqliteEngine getContext() {
        return engine;
    }
    public T get(String field, Object value) {
        T result = null;

        for (T t : list) {
            try {
                Object tFieldValue = SqliteUtil.getUnAccessible(field, t);
                if (tFieldValue != null && tFieldValue.equals(value)) {
                    result = t;
                    break;
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public T query(String field, Object value, Class<T> tClass) {
        return engine.select("SELECT * FROM " + table + " WHERE " + field + " = '" + value + "';", tClass);
    }
    @Override
    public T check(String field, Object value, Class<T> tClass) {
        T t = get(field, value);

        if (t == null) {
            t = query(field, value, tClass);
        }
        if (t == null) {
            init(value);
        }
        list.add(t);
        return t;
    }
    @Override
    public void save(T t) {
        engine.updateOrInsert(table, t);
    }
    @Override
    public void delete(T t) { engine.delete(table, t); }
    @Override
    public void save() {
        saveAll();
    }
    @Override
    public void load() {
        loadAll();
    }
}
