package com.nelsontron.pair.entity;

import java.util.Objects;

public class Gamer {
    private String id;

    public Gamer() {
        id = null;
    }

    public Gamer(String uuid) {
        id = uuid;
    }

    // getters
    public String getId() {
        return id;
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
        Gamer gamer = (Gamer) o;
        return Objects.equals(id, gamer.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
