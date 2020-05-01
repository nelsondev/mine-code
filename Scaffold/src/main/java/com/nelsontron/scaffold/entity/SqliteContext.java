package com.nelsontron.scaffold.entity;

import com.nelsontron.scaffold.Scaffold;
import com.nelsontron.sqlite.SqliteEngine;

public class SqliteContext extends SqliteEngine {

    private static final String provider = Scaffold.sqlProvider;
    private static final String dataPath = Scaffold.sqlDataPath;

    public SqliteContext(String name) {
        super(provider, dataPath, name);
    }
}
