package com.nelsontron.sqlite;

import java.io.File;

public class SqliteEngine extends SqliteDatabase {

    private String path;

    public SqliteEngine(String provider, String path, String name) {
        super(name, provider + path);
        this.path = path;

        mkdirs();
        create();
    }

    private boolean mkdirs() {
        return new File(path).mkdirs();
    }
}
