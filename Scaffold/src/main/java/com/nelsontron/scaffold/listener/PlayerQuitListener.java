package com.nelsontron.scaffold.listener;

import com.nelsontron.scaffold.entity.User;
import com.nelsontron.scaffold.provider.UserProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    UserProvider tests;

    public PlayerQuitListener(UserProvider tests) {
        this.tests = tests;
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e) {
        User user = tests.getUser(e.getPlayer());
        tests.save(user);
    }
}
