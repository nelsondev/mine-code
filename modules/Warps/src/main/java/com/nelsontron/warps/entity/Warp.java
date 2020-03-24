package com.nelsontron.warps.entity;

import com.nelsontron.core.util.LocationUtil;
import org.bukkit.Location;

import java.util.Objects;
import java.util.Random;

public class Warp {
    private int id;
    private String userId;
    private String name;
    private String category;
    private String location;

    public Warp() {
        id = 0;
        userId = null;
        name = null;
        category = null;
        location = null;
    }
    public Warp(User user, String name, String category, Location location) {
        id = new Random().nextInt(99999);
        userId = user.getId();
        this.name = name;
        this.category = category;
        this.location = LocationUtil.locationToString(location);
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
    public String getCategory() {
        return category;
    }
    public Location getLocation() {
        return LocationUtil.stringToLocation(location);
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
    public void setCategory(String category) {
        this.category = category;
    }
    public void setLocation(Location string) {
        this.location = LocationUtil.locationToString(string);
    }

    // methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warp warp = (Warp) o;
        return id == warp.id &&
                Objects.equals(userId, warp.userId) &&
                Objects.equals(name, warp.name) &&
                Objects.equals(category, warp.category) &&
                Objects.equals(location, warp.location);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, category, location);
    }
}
