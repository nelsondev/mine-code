package com.nelsontron.action.executor;

import com.nelsontron.action.Main;
import com.nelsontron.action.provider.ActionProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionExecutor implements CommandExecutor {
    private ActionProvider actions;
    public ActionExecutor(ActionProvider provider) {
        actions = provider;
    }

    // methods
    private boolean sendNotPlayer(CommandSender sender) {
        sender.sendMessage(Main.ERROR + "You must be a player to use this command.");
        return false;
    }
    private boolean sendInvalidArgs(CommandSender sender) {
        sender.sendMessage(Main.ERROR + "Invalid command arguments.");
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return sendNotPlayer(sender);

        Player player = (Player) sender;
        String command;

        // get what sub command the user wanted
        return true;
    }
}
