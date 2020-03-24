package com.nelsontron.warps.provider;

import com.nelsontron.core.data.StandardProvider;
import com.nelsontron.warps.entity.SqliteContext;
import com.nelsontron.warps.entity.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.util.UUID;

public class UserProvider extends StandardProvider<User> {
    private static final String table = "users";

    public UserProvider(SqliteContext context) {
        super(context, table);
    }

    // getters
    public User getUser(Player player) {
        return super.get("id", player.getUniqueId().toString());
    }

    // methods
    public User check(UUID uuid) { return check("id", uuid.toString(), User.class); }
    public User check(Player player) {
        return check(player.getUniqueId());
    }
    @Override
    public void init(Object o) {
        getList().add(new User(o.toString()));
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
