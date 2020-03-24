package com.nelsontron.action.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
}
