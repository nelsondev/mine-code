package com.nelsontron.pair.listener;

import com.nelsontron.pair.provider.GamerProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    GamerProvider gamers;

    public PlayerJoinListener(GamerProvider gamers) {
        this.gamers = gamers;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        gamers.check(e.getPlayer());
    }
}
