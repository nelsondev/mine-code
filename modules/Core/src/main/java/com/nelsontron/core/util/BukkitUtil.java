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
            if (!player.getName().equals(name)) continue;

            result = player;
            break;
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
}
