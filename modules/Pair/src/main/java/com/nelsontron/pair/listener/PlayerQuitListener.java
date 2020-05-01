package com.nelsontron.pair.listener;

import com.nelsontron.pair.entity.Gamer;
import com.nelsontron.pair.provider.GamerProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    GamerProvider gamers;

    public PlayerQuitListener(GamerProvider gamers) {
        this.gamers = gamers;
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e) {
        Gamer gamer = gamers.getGamer(e.getPlayer());
        gamers.save(gamer);
    }
}
