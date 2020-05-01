package com.nelsontron.scaffold.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DependencyLoader {
    private static List<String> searchForFileNameContainingSubstring(String path, String substring) {
        //This is assuming you pass in the substring from input.
        File file = new File(path); //Change this to the directory you want to search in.

        List<String> filesContainingSubstring = new ArrayList<>();

        if( file.exists() && file.isDirectory())
        {
            String[] files = file.list(); //get the files in String format.
            for( String fileName : files)
            {
                if( fileName.contains(substring) && fileName.contains(".jar"))
                    filesContainingSubstring.add(fileName);
            }
        }

        return filesContainingSubstring; //return the list of filenames containing substring.
    }
    private static boolean fileExists(String name) {
        return !searchForFileNameContainingSubstring("plugins", name).isEmpty();
    }
    private static boolean isOutdated(String name, String version) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        return plugin == null || !plugin.getDescription().getVersion().equals(version);
    }
    private static boolean download(String dir, String uri) {
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
    private static void load(String pluginDir, String name) {
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
        }
    }
}
