package com.nelsontron.sqlite;

import com.nelsontron.sqlite.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sqlite Utility Library
 * this class handles all the grunt work for getting, setting, updating and deleting of records in a sqlite database.
 * Everything works in an extremely object oriented matter relying on SqliteSerializable model objects. This is a static
 * utility library made for the sqlite database class to call from, but also to provide functionality to the project if
 * custom queries need to be executed.
 */
public class SqliteUtil {
    public static void createDatabase(String url) {
        try {
            DriverManager.getConnection(url).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void query(String url, String sql) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getFormattedId(String id) {
        String result = id;
        if (!StringUtil.isNumeric(result)) result = "'" + result + "'";
        return result;
    }
    private static String getFormattedId(SqliteSerializable obj) throws NoSuchFieldException, IllegalAccessException {
        String result = getUnAccessible("id", obj).toString();
        return getFormattedId(result);
    }
    /**
     * Get un accessible field
     * some fields may be accessed if they're privatized. This function grabs the field value from all fields.
     *
     * @param name - name of field to access
     * @param obj - object to access
     * @return - field result
     * @throws NoSuchFieldException - if field doesnt exist on obj
     * @throws IllegalAccessException - if field cannot be accessed in memory
     */
    private static Object getUnAccessible(String name, Object obj) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(name);
        if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
        return field.get(obj);
    }
    /**
     * Get class package name
     * if object is of type ArrayList, then the generic encapsulated type is hidden. This function grabs the full
     * package name of the generic array list.
     *
     * @param field - field to grab package name from
     * @return - full package name
     * @throws ClassCastException -
     */
    private static String getActualTypeName(Field field) throws ClassCastException {
        String typeName;
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        typeName = type.getActualTypeArguments()[0].getTypeName();

        return typeName;
    }
    /**
     * Get simplified class name
     * remove package from class type name
     *
     * @param type - full package name
     * @return - simplified type name
     */
    private static String getSimpleTypeName(String type) {
        if (type == null) return null;
        String[] tmp = type.split("\\.");
        return tmp[tmp.length - 1];
    }
    /**
     * Fill ArrayList object field
     * If field type is List<> generic type then fill list with referenced objects. The naming scheme for grabbing
     * referenced files goes as such:
     *
     *  Field name is kits. It's of List<Kit>
     *  - table - will be the lists generic holding type (Kit in this case) appended with an "s". Result is "kits"
     *  - kitId - will be the field referenced in the other table
     *
     * @param url - database file to connect to
     * @param t - object to apply field to
     * @param field - list field to be accessed
     * @param <T> - template object that contains list field
     * @throws IllegalAccessException - if field is private and cannot be changed
     * @throws NoSuchFieldException - if field doesn't exist on T template object
     * @throws ClassNotFoundException - if list type name cannot be referenced into a class.
     */
    private static <T> void fillListField(String url, T t, Field field) throws IllegalAccessException,
            NoSuchFieldException,
            ClassNotFoundException {
        // grab accessor class id
        Object id = getUnAccessible("id", t);
        // get the type name of what the list holds
        String typeName = getActualTypeName(field);
        // get simplified type name without package and such.
        String table = getSimpleTypeName(typeName).toLowerCase() + "s";
        // get the field name to look compare against.
        String fieldName = t.getClass().getSimpleName().toLowerCase() + "Id";

        // null checks
        if (id == null) return;
        if (typeName == null) return;

        // set field finally
        field.set(t, getReferences(url, table, id.toString(), fieldName, Class.forName(typeName)));
    }
    /**
     * Set Field
     * set an objects fields to their respective read values from a result set.
     *
     *  set accessible if private, fill list field if its a list, or set a field.
     *
     * @param url - database file to connect to
     * @param resultSet - result objects to hydrate fields with
     * @param t - object to be hydrated
     * @param declaredFields - template objects declared field list
     * @param <T> - template object to be hydrated
     * @throws SQLException - if db concurrency or sql error
     * @throws IllegalAccessException - if object field is private un accessible.
     * @throws ClassNotFoundException - if field class not found
     * @throws NoSuchFieldException - if field cannot be found in template object
     */
    private static <T> void setFields(String url, ResultSet resultSet, T t, Field[] declaredFields) throws SQLException,
            IllegalAccessException,
            ClassNotFoundException,
            NoSuchFieldException {
        for (Field field : declaredFields) {
            // field is modified private
            if (field.getName().substring(0, 1).equals("_")) continue;
            if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
            // if item is list, then populate it
            if (Collection.class.isAssignableFrom(field.getType())) {
                fillListField(url, t, field);
                continue;
            }

            field.set(t, resultSet.getObject(field.getName()));
        }
    }

    public static <T> T select(String url, String sql, Class<T> clazz) {
        T result = null;

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                result = clazz.newInstance();
                setFields(url, resultSet, result, clazz.getDeclaredFields());
            }

        } catch (SQLException
                | IllegalAccessException
                | InstantiationException
                | NoSuchFieldException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
    public static <T> T select(String url, String table, SqliteSerializable obj) {
        T result = null;
        try {
            result = (T) select(url, "SELECT * FROM " + table + " WHERE id=" + getFormattedId(obj) + ";", obj.getClass());
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static <T> List<T> selectAll(String url, String sql, Class<T> clazz) {
        List<T> result = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                T t = clazz.newInstance();
                setFields(url, resultSet, t, clazz.getDeclaredFields());
                result.add(t);
            }

        } catch (SQLException
                | IllegalAccessException
                | InstantiationException
                | NoSuchFieldException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <T> T get(String url, String table, String id, Class<T> clazz) {
        return select(url, "SELECT * FROM " + table + " WHERE id = " + getFormattedId(id) + ";", clazz);
    }
    public static <T> T getReference(String url, String table, String id, String fieldName, Class<T> clazz) {
        return select(url, "SELECT * FROM " + table + " WHERE " + fieldName + " = " + getFormattedId(id) + ";", clazz);
    }
    public static <T> List<T> getReferences(String url, String table, String id, String fieldName, Class<T> clazz)  {
        return selectAll(url, "SELECT * FROM " + table + " WHERE " + fieldName + " = " + getFormattedId(id) + ";", clazz);
    }

    public static void insert(String url, String table, SqliteSerializable obj) {
        String sql = null;
        try {
            sql = "INSERT OR REPLACE INTO " + table + "("
                    + obj.toKeySet()
                    + ") VALUES ("
                    + obj.toValueSet()
                    + ");";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        query(url, sql);
    }
    public static void insert(String url, String table, List<SqliteSerializable> list) {
        for (SqliteSerializable o : list) {
            insert(url, table, o);
        }
    }

    public static void update(String url, String table, SqliteSerializable obj) {
        String sql = null;
        try {
            String set = obj.toKeyValueSet(false);
            sql = "UPDATE " + table + " SET " + set + " WHERE id = " + getFormattedId(obj) + ";";
            updateReferences(url, table, obj);
            if (set == null) return;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        query(url, sql);
    }
    public static void update(String url, String table, String where, SqliteSerializable obj) {
        String sql = null;
        try {
            sql = "UPDATE " + table +
                    " SET " + obj.toKeyValueSet(false) +
                    " WHERE " + where + ";";
            updateReferences(url, table, obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        query(url, sql);
    }
    public static void update(String url, String table, List<SqliteSerializable> list) {
        for (SqliteSerializable obj : list) {
            update(url, table, obj);
        }
    }
    public static void updateReferences(String url, String table, SqliteSerializable obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getName().substring(0, 1).equals("_")) continue;
            if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
            if (!Collection.class.isAssignableFrom(field.getType())) continue;

            try {
                // grab accessor class id
                Object id = getUnAccessible("id", obj);
                // get the type name of what the list holds
                String typeName = getActualTypeName(field);
                // get simplified type name without package and such.
                String refTable = getSimpleTypeName(typeName).toLowerCase() + "s";
                // get the field name to look compare against.
                String fieldName = obj.getClass().getSimpleName().toLowerCase() + "Id";

                // null checks
                if (typeName == null) return;
                if (table == null) return;

                List<SqliteSerializable> list = (List<SqliteSerializable>) field.get(obj);
                List<SqliteSerializable> delete = new ArrayList<>();

                if (list == null) continue;
                List<?> temp = getReferences(url, refTable, id.toString(), fieldName, Class.forName(typeName));

                // delete records marked to remove
                for (Object o : temp) {
                    if (!(o instanceof SqliteSerializable)) continue;
                    if (list.contains(o)) continue;
                    delete(url, refTable, (SqliteSerializable) o);
                }

                // update records
                for (SqliteSerializable updating : list) {
                    updateOrInsert(url, refTable, updating);
                }
            } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException | ClassCastException e) {
                e.printStackTrace();
            }

        }
    }
    public static void updateOrInsert(String url, String table, SqliteSerializable obj) {
        if (select(url, table, obj) == null) insert(url, table, obj);
        else update(url, table, obj);
    }

    public static void delete(String url, String table, String where) {
        String sql = "DELETE FROM " + table + " WHERE " + where + ";";
        query(url, sql);
    }
    public static void delete(String url, String table, SqliteSerializable obj) {
        try {
            delete(url, table, "id = " + getFormattedId(obj));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    public static void delete(String url, String table, List<SqliteSerializable> list) {
        for (SqliteSerializable obj : list) {
            delete(url, table, obj);
        }
    }
}
