package com.nelsontron.kits.entity;

import com.nelsontron.sqlite.SqliteSerializable;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Kit extends SqliteSerializable {
    private int id;
    private String userId;
    private String name;
    private List<Item> items;

    public Kit() {
        id = 0;
        userId = null;
        name = null;
        items = null;
    }
    public Kit(User user, String name) {
        id = new Random().nextInt(9999);
        userId = user.getId();
        this.name = name;
    }

    // getters
    public int getId() {
        return id;
    }
    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
    public List<Item> getItems() {
        return items;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }

    // methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kit kit = (Kit) o;
        return id == kit.id &&
                Objects.equals(userId, kit.userId) &&
                Objects.equals(name, kit.name) &&
                Objects.equals(items, kit.items);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, items);
    }
}
