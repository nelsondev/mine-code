package com.nelsontron.action.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.Float.parseFloat;

public class LocationUtil {
    public static String locationToString(Location location) {
        if (location == null) return null;
        String world;
        String x;
        String y;
        String z;
        String yaw;

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);

        world = location.getWorld().getName();
        x = df.format(location.getX());
        y = df.format(location.getY());
        z = df.format(location.getZ());
        yaw = df.format(location.getYaw());

        return world + "," + x + "," + y + "," + z + "," + yaw;
    }
    public static Location stringToLocation(String string) {
        if (string == null) return null;
        String[] props = string.split(",");
        if (props.length == 0) return null;

        World world = Bukkit.getWorld(props[0]);
        float x = parseFloat(props[1]);
        float y = parseFloat(props[2]);
        float z = parseFloat(props[3]);
        float yaw = parseFloat(props[4]);

        return new Location(world, x, y, z, yaw, 0);
    }
}
