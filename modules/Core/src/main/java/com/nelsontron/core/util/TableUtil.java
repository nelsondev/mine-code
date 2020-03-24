package com.nelsontron.core.util;

import java.lang.reflect.Field;
import java.util.Collection;

public class TableUtil {
    public static String getSqlType(Field field) {
        String type = field.getType().getSimpleName();
        if (type.equalsIgnoreCase("String")) type = "TEXT";
        if (type.equalsIgnoreCase("Int")
            || type.equalsIgnoreCase("Boolean")) type = "INTEGER";
        return type;
    }

    public static <T> String getTable(String name, Class<T> tClass) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + name + " (");
        for (Field field : tClass.getDeclaredFields()) {
            field.setAccessible(true);
            // if begins with _ or is list, skip
            // else, this just appends the field name + the type then a comma.
            if (!field.getName().substring(0, 1).equals("_")
                && !Collection.class.isAssignableFrom(field.getType())) {

                // fieldName + sqlDataType
                sql.append(field.getName())
                        .append(" ")
                        .append(getSqlType(field));

                if (field.getName().equals("id")) {
                    sql.append(" PRIMARY KEY");
                }

                sql.append(",");
            }
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1));
        sql.append(");");
        return sql.toString();
    }
}
