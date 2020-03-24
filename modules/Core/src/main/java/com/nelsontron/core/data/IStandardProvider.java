package com.nelsontron.core.data;

import com.nelsontron.sqlite.SqliteSerializable;

public interface IStandardProvider<T> {
    T query(String field, Object value, Class<T> tClass);
    T check(String field, Object value, Class<T> tClass);
    void init(Object value);
    void saveAll();
    void loadAll();
    void save(T t);

    void delete(T t);

    void save();
    void load();
}
