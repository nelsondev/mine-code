package com.nelsontron.warps.executor;

import com.nelsontron.core.etc.Scheme;
import com.nelsontron.warps.provider.GlobalProvider;
import com.nelsontron.warps.provider.UserProvider;
import com.nelsontron.warps.entity.User;
import com.nelsontron.warps.entity.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarpExecutor implements CommandExecutor {
    private UserProvider tests;
    private GlobalProvider globalWarps;

    public WarpExecutor(UserProvider tests, GlobalProvider globalWarps) {
        this.tests = tests;
        this.globalWarps = globalWarps;
    }

    // methods
    private boolean sendNotPlayer(CommandSender sender) {
        sender.sendMessage(Scheme.ERROR + "You must be a player to use this command.");
        return true;
    }
    private boolean sendInvalidArgs(CommandSender sender) {
        sender.sendMessage(Scheme.ERROR + "Invalid command arguments.");
        return true;
    }
    private boolean sendWarpNotExist(CommandSender sender, String name) {
        sender.sendMessage(Scheme.ERROR + "\"" + name + "\" doesn't exist.");
        return true;
    }
    private boolean sendWarpExist(CommandSender sender, String name) {
        sender.sendMessage(Scheme.ERROR + "\"" + name + "\" already exists.");
        return true;
    }
    private boolean sendNoPermission(CommandSender sender) {
        sender.sendMessage(Scheme.ERROR + "No permission for this command.");
        return true;
    }

    // command results
    private boolean sendHelp(Player sender) {
        if (!sender.hasPermission("warp.help")) return sendNoPermission(sender);

        final String prefix = Scheme.MUTED + "   /warp " + Scheme.TEXT;

        sender.sendMessage(Scheme.MUTED + "Please note most of these commands have shorthand notations. An example being "
                + "/w instead of /warp, /w h to show warp help, or /w rm to remove a warp quickly.");
        sender.sendMessage(Scheme.PRIMARY + "Warp help:");
        sender.sendMessage(prefix + "<name>" + Scheme.INFO + " - teleport to a personal warp.");
        sender.sendMessage(prefix + "add <name>" + Scheme.INFO + " - create new warp.");
        sender.sendMessage(prefix + "rem <name>" + Scheme.INFO + " - remove warp.");
        sender.sendMessage(prefix + "name <name> <new>" + Scheme.INFO + " - edit warp name.");
        sender.sendMessage(Scheme.PRIMARY + "Warp global help:");
        sender.sendMessage(prefix + "global <name>" + Scheme.INFO + " - teleport to a global warp.");
        sender.sendMessage(prefix + "global add <name>" + Scheme.INFO + " - create global warp.");
        sender.sendMessage(prefix + "global rem <name>" + Scheme.INFO + " - remove global warp.");
        sender.sendMessage(prefix + "global name <name> <new>" + Scheme.INFO + " - edit global name.");

        return true;
    }
    private boolean sendWarps(Player player) {
        if (!player.hasPermission("warp.list")) return sendNoPermission(player);
        final String prefix = "    " + Scheme.INFO;
        List<Warp> warps = tests.getUser(player).getWarps();
        List<Warp> temp = new ArrayList<>();

        for (Warp w : warps) {
            if (!w.getCategory().equalsIgnoreCase("global")) {
                temp.add(w);
            }
        }

        if (temp.isEmpty()) {
            player.sendMessage(Scheme.WARN + "There's no warps here.. Try /warp help to see available commands.");
            return true;
        }

        player.sendMessage(Scheme.PRIMARY + "Warps:");
        for (Warp warp : temp) {
            player.sendMessage(prefix + warp.getName());
        }
        return true;
    }
    private boolean addWarp(Player player, String[] args) {
        if (!player.hasPermission("warp.add")) return sendNoPermission(player);
        if (args.length == 0) return sendInvalidArgs(player);

        User user = tests.getUser(player);
        Warp warp = user.getWarp(args[0]);

        if (warp != null) return sendWarpExist(player, args[0]);

        warp = new Warp(user, args[0], "default", player.getLocation());
        user.getWarps().add(warp);
        player.sendMessage(Scheme.INFO + "Created warp \"" + warp.getName() + ".\"");
        return true;
    }
    private boolean removeWarp(Player player, String[] args) {
        if (!player.hasPermission("warp.remove")) return sendNoPermission(player);
        if (args.length == 0) return sendInvalidArgs(player);

        User user = tests.getUser(player);
        Warp warp = user.getWarp(args[0]);

        if (warp == null) return sendWarpNotExist(player, args[0]);

        user.getWarps().remove(user.getWarp(args[0]));

        player.sendMessage(Scheme.INFO + "Removed warp \"" + args[0] + ".\"");
        return true;
    }
    private boolean renameWarp(Player player, String[] args) {
        if (!player.hasPermission("warp.name")) return sendNoPermission(player);
        User user = tests.getUser(player);
        Warp warp;

        if (args.length != 2) return sendInvalidArgs(player);

        warp = user.getWarp(args[0]);
        Warp check = user.getWarp(args[1]);
        if (check != null) return sendWarpExist(player, args[1]);
        warp.setName(args[1]);

        player.sendMessage(Scheme.INFO + "Renamed warp \"" + args[0] + "\" to \"" + args[1] + ".\"");
        return true;
    }
    private boolean warp(Player player, String name) {
        if (!player.hasPermission("warp.use")) return sendNoPermission(player);
        User user = tests.getUser(player);
        Warp warp = user.getWarp(name);

        if (warp == null) return sendWarpNotExist(player, name);

        player.sendMessage(Scheme.INFO + "Warping to \"" + warp.getName() + ".\"");
        player.teleportAsync(warp.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return true;
    }

    private boolean sendGlobalWarps(Player player) {
        if (!player.hasPermission("warp.global.list")) return sendNoPermission(player);
        final String prefix = "    " + Scheme.INFO;

        if (globalWarps.getList().isEmpty()) {
            player.sendMessage(Scheme.WARN + "There's no global warps here.. Try /warp help to see available commands.");
            return true;
        }

        player.sendMessage(Scheme.PRIMARY + "Global Warps:");

        for (Warp warp : globalWarps.getList()) {
            player.sendMessage(prefix + warp.getName());
        }
        return true;
    }
    private boolean addGlobalWarp(Player player, String[] args) {
        if (!player.hasPermission("warp.global.add")) return sendNoPermission(player);
        if (args.length == 0) return sendInvalidArgs(player);

        Warp warp = globalWarps.getWarp(args[0]);
        User user = tests.getUser(player);

        if (warp != null) return sendWarpExist(player, args[0]);

        warp = new Warp(user, args[0], "global", player.getLocation());
        globalWarps.getList().add(warp);

        player.sendMessage(Scheme.INFO + "Created global warp \"" + warp.getName() + ".\"");
        return true;
    }
    private boolean removeGlobalWarp(Player player, String[] args) {
        if (!player.hasPermission("warp.global.remove")) return sendNoPermission(player);
        if (args.length == 0) return sendInvalidArgs(player);

        Warp warp = globalWarps.getWarp(args[0]);
        if (warp == null) return sendWarpNotExist(player, args[0]);
        globalWarps.getList().remove(warp);

        player.sendMessage(Scheme.INFO + "Removed global warp \"" + args[0] + ".\"");
        return true;
    }
    private boolean renameGlobalWarp(Player player, String[] args) {
        if (!player.hasPermission("warp.global.name")) return sendNoPermission(player);
        if (args.length != 2) return sendInvalidArgs(player);

        Warp warp = globalWarps.getWarp(args[0]);
        Warp check = globalWarps.getWarp(args[1]);
        if (check != null) return sendWarpExist(player, args[1]);
        warp.setName(args[1]);

        player.sendMessage(Scheme.INFO + "Renamed global warp \"" + args[0] + "\" to \"" + args[1] + ".\"");
        return true;
    }
    private boolean warpGlobal(Player player, String name) {
        if (!player.hasPermission("warp.global.use")) return sendNoPermission(player);
        Warp warp = globalWarps.getWarp(name);

        if (warp == null) return sendWarpNotExist(player, name);

        player.sendMessage(Scheme.INFO + "Warping to global \"" + warp.getName() + ".\"");
        player.teleportAsync(warp.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return sendNotPlayer(sender);

        Player player = (Player) sender;
        String command;

        // get what sub command the user wanted
        if (args.length == 0) return sendWarps(player);
        command = args[0];
        args = Arrays.copyOfRange(args,  1, args.length);

        // sub commands
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
            || command.equalsIgnoreCase("rn")
            || command.equalsIgnoreCase("n")) return renameWarp(player, args);

        // global warping
        if (command.equalsIgnoreCase("global")
            || command.equalsIgnoreCase("g")) {

            // get what sub command the user wanted
            if (args.length == 0) return sendGlobalWarps(player);
            command = args[0];
            args = Arrays.copyOfRange(args, 1, args.length);

            // sub commands
            if (command.equalsIgnoreCase("add")
                    || command.equalsIgnoreCase("set")
                    || command.equalsIgnoreCase("create")
                    || command.equalsIgnoreCase("a")
                    || command.equalsIgnoreCase("s")
                    || command.equalsIgnoreCase("c")) return addGlobalWarp(player, args);
            if (command.equalsIgnoreCase("remove")
                    || command.equalsIgnoreCase("delete")
                    || command.equalsIgnoreCase("r")
                    || command.equalsIgnoreCase("d")) return removeGlobalWarp(player, args);
            if (command.equalsIgnoreCase("rename")
                    || command.equalsIgnoreCase("name")
                    || command.equalsIgnoreCase("rn")
                    || command.equalsIgnoreCase("n")) return renameGlobalWarp(player, args);

            return warpGlobal(player, command);
        }

        return warp(player, command);
    }
}
