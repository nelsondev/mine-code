package com.nelsontron.kits.listener;

import com.nelsontron.kits.UserProvider;
import com.nelsontron.kits.entity.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    UserProvider users;

    public PlayerJoinListener(UserProvider users) {
        this.users = users;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (users.getUser(player) == null) users.getUsers().add(new User(player));
    }
}
