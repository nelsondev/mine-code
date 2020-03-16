package com.nelsontron.warps.executor;

import com.nelsontron.warps.Main;
import com.nelsontron.warps.UserProvider;
import com.nelsontron.warps.entity.User;
import com.nelsontron.warps.entity.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;

public class WarpExecutor implements CommandExecutor {
    private UserProvider users;

    public WarpExecutor(UserProvider provider) {
        users = provider;
    }

    // methods
    boolean sendInvalidArgs(CommandSender sender) {
        sender.sendMessage(Main.ERROR + "Invalid command arguments.");
        return false;
    }
    boolean sendWarpNotExist(CommandSender sender, String name) {
        sender.sendMessage(Main.ERROR + "\"" + name + "\" doesn't exist.");
        return false;
    }
    boolean sendWarpExist(CommandSender sender, String name) {
        sender.sendMessage(Main.ERROR + "\"" + name + "\" already exists.");
        return false;
    }
    boolean sendHelp(Player sender) {
        final String prefix = Main.MUTED + "   /warp " + Main.TEXT;

        sender.sendMessage(Main.TEXT + "Warp command help:");
        sender.sendMessage(prefix + "help" + Main.INFO + " - display this message.");
        sender.sendMessage(prefix + "add <name>" + Main.INFO + " - create a new warp.");
        sender.sendMessage(prefix + "remove <name>" + Main.INFO + " - remove a warp.");
        sender.sendMessage(prefix + "rename <name> <new name>" + Main.INFO + " - edit a warp name.");
        sender.sendMessage(prefix + "category <name> <new category>" + Main.INFO + " - edit a warp category.");
        return true;
    }
    boolean sendWarps(Player player) {
        final String prefix = "    " + Main.TEXT;
        player.sendMessage(Main.TEXT + "Warps:");

        User user = users.getUser(player);
        for (Warp warp : user.getWarps()) {
            player.sendMessage(prefix + warp.getName());
        }

        return true;
    }
    boolean addWarp(Player player, String[] args) {
        if (args.length == 0) return sendInvalidArgs(player);

        User user = users.getUser(player);
        Warp warp = user.getWarp(args[0]);

        if (warp != null) return sendWarpExist(player, args[0]);

        warp = new Warp(user, args[0], "default", player.getLocation());
        user.getWarps().add(warp);
        player.sendMessage(Main.TEXT + "Created warp " + warp.getName() + ".");

        return true;
    }
    boolean removeWarp(Player player, String[] args) {
        if (args.length == 0) return sendInvalidArgs(player);

        User user = users.getUser(player);
        Warp warp = user.getWarp(args[0]);

        if (warp == null) return sendWarpNotExist(player, args[0]);

        user.getWarps().remove(user.getWarp(args[0]));

        player.sendMessage(Main.TEXT + "Removed warp " + args[0] + ".");

        return true;
    }
    private boolean renameWarp(Player player, String[] args) {
        return true;
    }
    private boolean categorizeWarp(Player player, String[] args) {
        return true;
    }
    boolean warp(Player player, String name) {
        User user;
        Warp warp;
        user = users.getUser(player);
        warp = user.getWarp(name);

        if (warp == null) return sendWarpNotExist(player, name);

        player.sendMessage("Warping to \"" + warp.getName() + ".\"");
        player.teleportAsync(warp.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        assert sender instanceof Player;

        Player player = (Player) sender;
        String command;

        if (args.length == 0) return sendWarps(player);
        command = args[0];
        args = Arrays.copyOfRange(args,  1, args.length);

        if (command.equalsIgnoreCase("help")
            || command.equalsIgnoreCase("h")) return sendHelp(player);
        if (command.equalsIgnoreCase("add")
            || command.equalsIgnoreCase("set")
            || command.equalsIgnoreCase("create")
            || command.equalsIgnoreCase("a")
            || command.equalsIgnoreCase("s")
            || command.equalsIgnoreCase("c")) return addWarp(player, args);
        if (command.equalsIgnoreCase("remove")
            || command.equalsIgnoreCase("delete")
            || command.equalsIgnoreCase("r")
            || command.equalsIgnoreCase("d")) return removeWarp(player, args);
        if (command.equalsIgnoreCase("rename")
            || command.equalsIgnoreCase("name")
            || command.equalsIgnoreCase("rn")) return renameWarp(player, args);
        if (command.equalsIgnoreCase("category")
                || command.equalsIgnoreCase("cat")
                || command.equalsIgnoreCase("ca")) return categorizeWarp(player, args);

        return warp(player, command);
    }
}
