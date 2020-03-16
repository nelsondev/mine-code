package com.nelsontron.kits.entity;

import com.nelsontron.sqlite.SqliteSerializable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends SqliteSerializable {
    private String id;
    private List<Kit> kits;

    public User() {
        id = null;
        kits = null;
    }
    public User(String string) {
        id = string;
        kits = new ArrayList<>();
    }
    public User(Player player) {
        id = player.getUniqueId().toString();
        kits = new ArrayList<>();
    }

    // getters
    public String getId() {
        return id;
    }
    public List<Kit> getKits() {
        return kits;
    }
    public Kit getKit(String name) {
        Kit result = null;
        for (Kit k : kits) {
            if (k.getName().equals(name)) {
                result = k;
                break;
            }
        }
        return result;
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(id);
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setKits(List<Kit> kits) {
        this.kits = kits;
    }

    // methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(kits, user.kits);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, kits);
    }
}
