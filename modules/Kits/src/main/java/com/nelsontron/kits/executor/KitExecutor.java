package com.nelsontron.kits.executor;

import com.nelsontron.kits.Main;
import com.nelsontron.kits.UserProvider;
import com.nelsontron.kits.entity.Kit;
import com.nelsontron.kits.entity.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class KitExecutor implements CommandExecutor {

    UserProvider users;

    public KitExecutor(UserProvider users) {
        this.users = users;
    }

    boolean sendNotPlayer(CommandSender sender) {
        sender.sendMessage(Main.ERROR + "You must be a player to use this command.");
        return false;
    }
    boolean sendInvalidArgs(CommandSender sender) {
        sender.sendMessage(Main.ERROR + "Invalid command arguments.");
        return false;
    }
    boolean sendKitNotExist(CommandSender sender, String name) {
        sender.sendMessage(Main.ERROR + "\"" + name + "\" doesn't exist.");
        return false;
    }
    boolean sendKitExist(CommandSender sender, String name) {
        sender.sendMessage(Main.ERROR + "\"" + name + "\" already exists.");
        return false;
    }
    boolean sendHelp(Player sender) {
        final String prefix = Main.MUTED + "   /kit " + Main.TEXT;

        sender.sendMessage(Main.TEXT + "Kit command help:");
        sender.sendMessage(prefix + "help" + Main.INFO + " - display this message.");
        sender.sendMessage(prefix + "add <name>" + Main.INFO + " - create a new kit.");
        sender.sendMessage(prefix + "remove <name>" + Main.INFO + " - remove a kit.");
        sender.sendMessage(prefix + "edit <name>" + Main.INFO + " - edit a kit.");
        sender.sendMessage(prefix + "");
        return true;
    }
    boolean sendKits(Player sender) {
        final String prefix = "    " + Main.TEXT;
        sender.sendMessage(Main.TEXT + "Kits:");

        User user = users.getUser(sender);
        for (Kit kit : user.getKits()) {
            sender.sendMessage(prefix + kit.getName());
        }

        return true;
    }
    boolean addKit(Player player, String[] args) {
        if (args.length == 0) return sendInvalidArgs(player);

        User user = users.getUser(player);
        Kit kit = user.getKit(args[0]);

        if (kit != null) return sendKitExist(player, args[0]);

        kit = new Kit(user, args[0]);

        user.getKits().add(kit);

        player.sendMessage(Main.TEXT + "Created kit " + kit.getName() + ".");

        return true;
    }
    boolean removeKit(Player player, String[] args) {
        if (args.length == 0) return sendInvalidArgs(player);

        User user = users.getUser(player);
        Kit kit = user.getKit(args[0]);

        if (kit == null) return sendKitNotExist(player, args[0]);

        user.getKits().remove(user.getKit(args[0]));

        player.sendMessage(Main.TEXT + "Removed kit " + args[0] + ".");

        return true;
    }

    // handler
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player;
        String command;

        if (!(sender instanceof Player)) return sendNotPlayer(sender);
        player = (Player) sender;

        if (args.length == 0) return sendKits(player);
        command = args[0];
        args = Arrays.copyOfRange(args,  1, args.length);

        if (command.equalsIgnoreCase("help")) return sendHelp(player);
        if (command.equalsIgnoreCase("add")) return addKit(player, args);
        if (command.equalsIgnoreCase("remove")) return removeKit(player, args);

        return false;
    }
}
