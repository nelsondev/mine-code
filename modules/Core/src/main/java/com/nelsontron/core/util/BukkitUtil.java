package com.nelsontron.core.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class BukkitUtil {
    public static void registerCommands(JavaPlugin plugin, CommandExecutor executor, String ...commands) {
        for (String label : commands) {

            PluginCommand command = plugin.getCommand(label);

            if (command == null) {
                plugin.getLogger().severe("COMMAND \"" + label + "\" couldn't be registered...");
                return;
            }

            command.setExecutor(executor);
        }
    }

    public static Player getPlayer(String name) {
        Player result = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getDisplayName().equalsIgnoreCase(name)) {
                result = player;
                break;
            }
        }
        return result;
    }

    public static ChatColor getChatColor(String name) {
        ChatColor result = null;
        for (ChatColor c : ChatColor.class.getEnumConstants()) {
            if (c.name().equalsIgnoreCase(name)) {
                result = c;
            }
        }
        return result;
    }

    public static FileConfiguration getSqliteConfig(JavaPlugin plugin) {
        InputStream stream = plugin.getResource("sqlite.yml");
        InputStreamReader reader;
        reader = new InputStreamReader(Objects.requireNonNull(stream));
        return YamlConfiguration.loadConfiguration(reader);
    }

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        //
        // In particular, do NOT do 'Random rand = new Random()' here or you
        // will get not very good / not very random results.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
