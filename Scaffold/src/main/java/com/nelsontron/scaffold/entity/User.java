package com.nelsontron.scaffold.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class User {
    private String id;

    public User() {
        id = null;
    }
    public User(String string) {
        id = string;
    }
    public User(Player player) {
        id = player.getUniqueId().toString();
    }

    // getters
    public String getId() {
        return id;
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(id);
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }

    // methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
