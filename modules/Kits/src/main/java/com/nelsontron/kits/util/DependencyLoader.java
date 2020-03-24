package com.nelsontron.kits.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.URL;

public class DependencyLoader {
    public static boolean fileExists(String name) {
        return new File("plugins/" + name + ".jar").exists();
    }
    public static boolean isOutdated(String name, String version) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        return plugin == null || !plugin.getDescription().getVersion().equals(version);
    }
    public static boolean download(String dir, String uri) {
        new File(dir).getParentFile().mkdirs();
        URL url;
        InputStream in;
        OutputStream out;
        byte[] buffer = new byte[1024];

        try {
            url = new URL(uri);
            in = url.openStream();
            out = new FileOutputStream(dir + ".jar");

            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void load(String pluginDir, String name) {
        try {
            Plugin plugin = Bukkit.getPluginManager().loadPlugin(new File(pluginDir + name + ".jar"));
            if (plugin == null) {
                Bukkit.getLogger().severe("[Dependencies] Unable to load plugin dependency \"" + name + ".\""
                        + " Shutting down server!");
                return;
            }
            Bukkit.getPluginManager().enablePlugin(plugin);
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            e.printStackTrace();
        }
    }
    public static void check(String name, String version, String uri) {
        if (!fileExists(name)) {
            download("plugins/" + name, uri);
            load("plugins/", name);
        } else if (isOutdated(name, version)) {
            new File("plugins/" + name + ".jar").delete();
            download("plugins/" + name, uri);
            load("plugins/", name);
        }
    }
}
