package com.nelsontron.sqlite;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SqliteSerializable {

    public static Field[] withoutId(Object object) {
        Field[] result;
        List<Field> list = new ArrayList<>();
        for (int i = 0; i < object.getClass().getDeclaredFields().length; i++) {
            Field field = object.getClass().getDeclaredFields()[i];

            if (Modifier.isPrivate(field.getModifiers()))
                field.setAccessible(true);

            if (field.getName().equals("id")) continue;
            list.add(field);
        }
        result = new Field[list.size()];
        return list.toArray(result);
    }
    public static String toKeySet(Object object) {
        return toKeySet(object, true);
    }
    public static String toKeySet(Object object, boolean withId) {
        StringBuilder sb = new StringBuilder();

        Field[] fields;

        if (withId)
            fields = object.getClass().getDeclaredFields();
        else
            fields = withoutId(object);

        for (Field field : fields) {
            // field is modified private
            if (field.getName().substring(0, 1).equals("_")) continue;
            if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
            if (Collection.class.isAssignableFrom(field.getType())) continue;

            sb.append(field.getName()).append(",");
        }

        return sb.toString().substring(0, sb.length()-1);
    }
    public static String toValueSet(Object object) throws IllegalAccessException {
        return toValueSet(object, true);
    }
    public static String toValueSet(Object object, boolean withId) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();

        Field[] fields;

        if (withId)
            fields = object.getClass().getDeclaredFields();
        else
            fields = withoutId(object);

        for (Field field : fields) {
            // field is modified private
            if (field.getName().substring(0, 1).equals("_")) continue;
            if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
            if (Collection.class.isAssignableFrom(field.getType())) continue;

            Object val = field.get(object);
            if (val instanceof String) {
                sb.append("'").append(val).append("'");
            }
            else {
                sb.append(val);
            }
            sb.append(",");
        }

        return sb.toString().substring(0, sb.length()-1);
    }
    public static String toKeyValueSet(Object object) throws IllegalAccessException {
        return toKeyValueSet(object, true);
    }
    public static String toKeyValueSet(Object object, boolean withId) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();

        Field[] fields;

        if (withId)
            fields = object.getClass().getDeclaredFields();
        else
            fields = withoutId(object);

        for (Field field : fields) {
            // field is modified private
            if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
            if (field.getName().substring(0, 1).equals("_")) continue;
            if (Collection.class.isAssignableFrom(field.getType())) continue;

            String key = field.getName();
            Object val = field.get(object);

            sb.append(key);
            sb.append("=");

            if (val instanceof String) {
                sb.append("'").append(val).append("'");
            }
            else {
                sb.append(val);
            }

            sb.append(",");
        }

        if (sb.toString().length() > 0)
            return sb.toString().substring(0, sb.length()-1);
        else
            return null;
    }
}
