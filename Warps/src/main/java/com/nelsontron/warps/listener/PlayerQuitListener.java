package com.nelsontron.warps.listener;

import com.nelsontron.warps.UserProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    UserProvider users;

    public PlayerQuitListener(UserProvider users) {
        this.users = users;
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        users.saveUser(users.getUser(player));
    }
}
