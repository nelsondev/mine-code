package com.nelsontron.warps.listener;

import com.nelsontron.warps.provider.UserProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    UserProvider tests;

    public PlayerJoinListener(UserProvider tests) {
        this.tests = tests;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        tests.check(e.getPlayer());
    }
}
