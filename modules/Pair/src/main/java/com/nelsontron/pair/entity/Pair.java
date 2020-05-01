package com.nelsontron.pair.entity;

import com.nelsontron.core.util.BukkitUtil;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Pair {
    private int id;
    private String player1;
    private String player2;

    public Pair() {
        id = 0;
        player1 = null;
        player2 = null;
    }

    public Pair(String player1, String player2) {
        this.id = new Random().nextInt(99999);
        this.player1 = player1;
        this.player2 = player2;
    }

    // getters
    public int getId() {
        return id;
    }
    public String getPlayerUuid1() {
        return player1;
    }
    public String getPlayerUuid2() {
        return player2;
    }
    public Player getPlayer1() {
        Player result = null;
        try {
            result = Bukkit.getPlayer(UUID.fromString(player1));
        } catch (IllegalArgumentException ignored) { }
        return result;
    }
    public Player getPlayer2() {
        Player result = null;
        try {
            result = Bukkit.getPlayer(UUID.fromString(player2));
        } catch (IllegalArgumentException ignored) { }
        return result;
    }
    public boolean isPlaying() {
        if (player1 == null) return false;
        if (player2 == null) return false;
        if (getPlayer1() == null) return false;
        if (getPlayer2() == null) return false;

        return true;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setPlayer1(String player1) {
        this.player1 = player1;
    }
    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    // methods
    @Override
    public String toString() {
        return "Pair{ " +
                "" + getPlayer1().getName()  +
                ", " + getPlayer2().getName() +
                " }";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return id == pair.id &&
                Objects.equals(player1, pair.player1) &&
                Objects.equals(player2, pair.player2);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, player1, player2);
    }
}
