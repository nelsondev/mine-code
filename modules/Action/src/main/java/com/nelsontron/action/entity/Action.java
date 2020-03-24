package com.nelsontron.action.entity;

import com.nelsontron.action.util.LocationUtil;
import com.nelsontron.sqlite.SqliteSerializable;
import org.bukkit.Location;

import java.util.Random;

public class Action extends SqliteSerializable {
    private int id;
    private String command;
    private String location;
    private boolean enabled;

    public Action() {
        id = new Random().nextInt(99999);
        command = null;
        location = null;
    }

    public Action(String command, Location location) {
        id = new Random().nextInt(99999);
        this.command = command;
        this.location = LocationUtil.locationToString(location);
    }

    // getters
    public int getId() {
        return id;
    }
    public String getCommand() {
        return command;
    }
    public Location getLocation() {
        return LocationUtil.stringToLocation(location);
    }
    public String getLocationString() { return location; }
    public boolean isEnabled() {
        return enabled;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public void setLocation(Location location) {
        this.location = LocationUtil.locationToString(location);
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
