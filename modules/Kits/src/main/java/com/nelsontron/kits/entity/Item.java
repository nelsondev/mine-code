package com.nelsontron.kits.entity;

import com.nelsontron.sqlite.SqliteSerializable;

import java.util.Objects;
import java.util.Random;

public class Item extends SqliteSerializable {
    private int id;
    private int kitId;
    private String name;
    private String desc;
    private String category;
    private int price;

    // item stuff
    private String type;
    private int amount;

    public Item() {
        id = 0;
        kitId = 0;
        name = null;
        category = null;
        price = 0;
        type = null;
        amount = 0;
    }
    public Item(Kit kit) {
        id = new Random().nextInt(9999);
        kitId = kit.getId();
        name = null;
        category = null;
        price = 0;
        type = null;
        amount = 0;
    }

    // setters
    public int getId() {
        return id;
    }
    public int getKitId() {
        return kitId;
    }
    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }
    public String getCategory() {
        return category;
    }
    public int getPrice() {
        return price;
    }
    public String getType() {
        return type;
    }
    public int getAmount() {
        return amount;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setKitId(int kitId) {
        this.kitId = kitId;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    // methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id &&
                kitId == item.kitId &&
                price == item.price &&
                amount == item.amount &&
                Objects.equals(name, item.name) &&
                Objects.equals(desc, item.desc) &&
                Objects.equals(category, item.category) &&
                Objects.equals(type, item.type);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, kitId, name, desc, category, price, type, amount);
    }
}
