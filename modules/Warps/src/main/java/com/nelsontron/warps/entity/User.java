package com.nelsontron.warps.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String id;
    private List<Warp> warps;

    public User() {
        id = null;
        warps = null;
    }
    public User(String string) {
        id = string;
        warps = new ArrayList<>();
    }
    public User(Player player) {
        id = player.getUniqueId().toString();
        warps = new ArrayList<>();
    }

    // getters
    public String getId() {
        return id;
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(id);
    }
    public List<Warp> getWarps() {
        return warps;
    }
    public Warp getWarp(Warp warp) {
        Warp result = null;
        for (Warp w : warps) {
            if (w.equals(warp)) {
                result = w;
                break;
            }
        }
        return result;
    }
    public Warp getWarp(String name) {
        Warp warp = null;
        for (Warp w : warps) {
            if (w.getName().equalsIgnoreCase(name)) {
                warp = w;
                break;
            }
        }
        return warp;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setWarps(List<Warp> warps) {
        this.warps = warps;
    }

    // methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(warps, user.warps);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, warps);
    }
}
