package com.nelsontron.warps.listener;

import com.nelsontron.warps.UserProvider;
import com.nelsontron.warps.entity.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
