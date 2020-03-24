package com.nelsontron.action.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.URL;
import java.util.Objects;

public class DependencyLoader {
    public static void check(String name, String uri) {
        if (!satisfied(name)) download(name, uri);
    }
    public static boolean satisfied(String name) {
        return new File("plugins/" + name + ".jar").exists();
    }
    public static void download(String name, String uri) {
        String pluginDir = "plugins/" + name + ".jar";
        try {
            URL url = new URL(uri);
            InputStream in = url.openStream();
            byte[] buffer = new byte[1014];
            OutputStream out = new BufferedOutputStream(new FileOutputStream(pluginDir));

            int read;
            while((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            out.flush();

            Bukkit.getPluginManager().loadPlugin(new File(pluginDir));

            Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

            if (plugin == null) {
                Bukkit.getLogger().severe("[DependencyLoader] Unable to patch dependency \"" + name
                    + ".\" Consider reloading the server as some features might not work.");
                return;
            }

            Bukkit.getPluginManager().enablePlugin(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(name)));
        } catch (IOException | InvalidDescriptionException | InvalidPluginException e) {
            e.printStackTrace();
        }
    }
}
