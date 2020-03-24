package com.nelsontron.warps.listener;

import com.nelsontron.warps.provider.UserProvider;
import com.nelsontron.warps.entity.User;
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
