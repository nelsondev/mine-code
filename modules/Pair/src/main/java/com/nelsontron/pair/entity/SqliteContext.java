package com.nelsontron.pair.entity;

import com.nelsontron.pair.Main;
import com.nelsontron.sqlite.SqliteEngine;

public class SqliteContext extends SqliteEngine {

    private static final String provider = Main.sqlProvider;
    private static final String dataPath = Main.sqlDataPath;

    public SqliteContext(String name) {
        super(provider, dataPath, name);
    }
}
