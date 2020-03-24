package com.nelsontron.warps.entity;

import com.nelsontron.sqlite.SqliteEngine;
import com.nelsontron.warps.Main;

public class SqliteContext extends SqliteEngine {

    private static final String provider = Main.sqlProvider;
    private static final String dataPath = Main.sqlDataPath;

    public SqliteContext(String name) {
        super(provider, dataPath, name);
    }
}
