package com.nelsontron.sqlite;

import com.nelsontron.sqlite.obj.Test;
import com.nelsontron.sqlite.obj.TestReference;

import java.io.File;
import java.util.Arrays;

class SqliteUtilTest {

    static String provider = "jdbc:sqlite:";
    static String dir = "D:/Tests/SqliteFramework/";
    static String database = "test.db";
    static String url = provider + dir + database;

    @org.junit.jupiter.api.Test
    void createDatabase() {
        SqliteUtil.createDatabase(url);
        assert new File(dir + database).exists();
    }

    @org.junit.jupiter.api.Test
    void query() {
        SqliteUtil.query(url, "DROP TABLE IF EXISTS tests;");
        SqliteUtil.query(url, "DROP TABLE IF EXISTS testReferences;");
        SqliteUtil.query(url, "CREATE TABLE tests (id INTEGER PRIMARY KEY, val TEXT);");
        SqliteUtil.query(url, "CREATE TABLE testReferences (id INTEGER PRIMARY KEY, testId INTEGER);");
        SqliteUtil.query(url, "INSERT INTO tests (id, val) VALUES (5, 'test value');");
        SqliteUtil.query(url, "INSERT INTO testReferences (id, testId) VALUES (3, 5);");
        assert SqliteUtil.select(url, "SELECT * FROM tests WHERE id = 5;", Test.class) != null;
        assert SqliteUtil.select(url, "SELECT * FROM testReferences WHERE id = 3;", TestReference.class) != null;
    }

    @org.junit.jupiter.api.Test
    void getUnAccessible() throws NoSuchFieldException, IllegalAccessException {

        Test test = new Test();
        test.setId(10);

        assert (int) SqliteUtil.getUnAccessible("id", test) == 10;
    }

    @org.junit.jupiter.api.Test
    void select() {
        query();

        Test test = new Test();
        test.setId(5);

        String table = "tests";
        String id = "5";
        String sql = "SELECT * FROM " + table + " WHERE id = " + id + ";";

        assert SqliteUtil.select(url, sql, Test.class) != null;
        assert !SqliteUtil.select(url, sql, Test.class).getTestReferences().isEmpty();
        assert SqliteUtil.select(url, sql, Test.class).getTestReferences().get(0).getId() == 3;

        assert SqliteUtil.select(url, table, test) != null;
        assert !SqliteUtil.select(url, table, test).getTestReferences().isEmpty();
        assert SqliteUtil.select(url, table, test).getTestReferences().get(0).getId() == 3;
    }

    @org.junit.jupiter.api.Test
    void selectAll() {
        query();

        String table = "tests";
        String sql = "SELECT * FROM " + table + ";";

        assert !SqliteUtil.selectAll(url, sql, Test.class).isEmpty();
        assert SqliteUtil.selectAll(url, sql, Test.class).get(0).getId() == 5;
    }

    @org.junit.jupiter.api.Test
    void get() {
        query();

        String table = "tests";
        String id = "5";

        assert SqliteUtil.get(url, table, id, Test.class) != null;
        assert !SqliteUtil.get(url, table, id, Test.class).getTestReferences().isEmpty();
        assert SqliteUtil.get(url, table, id, Test.class).getTestReferences().get(0).getId() == 3;
    }

    @org.junit.jupiter.api.Test
    void getReference() {
        query();

        String table = "testReferences";
        String id = "5"; // test reference to find
        String fieldName = "testId"; // TestReference field to refer to

        assert SqliteUtil.getReference(url, table, id, fieldName, TestReference.class) != null;
        assert SqliteUtil.getReference(url, table, id, fieldName, TestReference.class).getId() == 3;
    }

    @org.junit.jupiter.api.Test
    void getReferences() {
        query();

        String table = "testReferences";
        String id = "5"; // test reference to find
        String fieldName = "testId"; // TestReference field to refer to

        assert !SqliteUtil.getReferences(url, table, id, fieldName, TestReference.class).isEmpty();
        assert SqliteUtil.getReferences(url, table, id, fieldName, TestReference.class).get(0).getId() == 3;
    }

    @org.junit.jupiter.api.Test
    void insert() {
        query();

        Test test = new Test();
        test.setId(10);
        test.setVal("some value");

        Test test1 = new Test();
        test1.setId(11);
        test1.setVal("other value");

        Test test2 = new Test();
        test2.setId(12);
        test2.setVal("another");

        String table = "tests";
        String sql = "SELECT * FROM " + table + " WHERE ";

        SqliteUtil.insert(url, table, test);
        SqliteUtil.insert(url, table, Arrays.asList(new Test[] { test1, test2 }));

        assert SqliteUtil.select(url, sql + "id = " + test.getId() + ";", Test.class) != null;
        assert SqliteUtil.select(url, sql + "val = '" + test.getVal() + "';", Test.class) != null;
        assert SqliteUtil.select(url, sql + "id = " + test1.getId() + ";", Test.class) != null;
        assert SqliteUtil.select(url, sql + "val = '" + test1.getVal() + "';", Test.class) != null;
        assert SqliteUtil.select(url, sql + "id = " + test2.getId() + ";", Test.class) != null;
        assert SqliteUtil.select(url, sql + "val = '" + test2.getVal() + "';", Test.class) != null;
    }

    @org.junit.jupiter.api.Test
    void update() {
        query();

        String table = "tests";
        String id = "5";

        Test test = SqliteUtil.get(url, table, id, Test.class);
        test.setVal("a new value");

        SqliteUtil.update(url, table, test);

        assert SqliteUtil.get(url, table, id, Test.class).getVal().equals("a new value");

        test.setVal("a new new value");
        SqliteUtil.update(url, table, "id = " + id, test);

        assert SqliteUtil.get(url, table, id, Test.class).getVal().equals("a new new value");
    }

    @org.junit.jupiter.api.Test
    void delete() {
        query();

        String table = "tests";
        String id = "5";

        Test test = SqliteUtil.get(url, table, id, Test.class);

        SqliteUtil.delete(url, table, test);
        assert SqliteUtil.get(url, table, id, Test.class) == null;
    }
}