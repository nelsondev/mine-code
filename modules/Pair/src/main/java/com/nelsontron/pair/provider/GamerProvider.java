package com.nelsontron.pair.provider;

import com.nelsontron.core.data.StandardProvider;
import com.nelsontron.pair.entity.Gamer;
import com.nelsontron.sqlite.SqliteEngine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamerProvider extends StandardProvider<Gamer> {

    public static final String table = "gamers";

    public GamerProvider(SqliteEngine engine) {
        super(engine, table);
    }

    // getters
    public Gamer getGamer(String uuid) { return super.get("id", uuid); }
    public Gamer getGamer(Player player) {
        return super.get("id", player.getUniqueId().toString());
    }

    // methods
    public Gamer check(UUID uuid) { return check("id", uuid.toString(), Gamer.class); }
    public Gamer check(Player player) {
        return check(player.getUniqueId());
    }

    // saving / loading
    @Override
    public void init(Object o) {
        getList().add(new Gamer(o.toString()));
    }
    @Override
    public void saveAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            save(check(p));
        }
    }
    @Override
    public void loadAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            check(p);
        }
    }
}
