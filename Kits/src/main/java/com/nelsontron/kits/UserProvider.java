package com.nelsontron.kits;

import com.nelsontron.kits.entity.User;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserProvider {

    private final String DATABASE = "kits.db";
    private final SqliteContext context;
    private final List<User> users;

    public UserProvider() {
        context = new SqliteContext(DATABASE);
        users = new ArrayList<>();
    }

    // getters
    public List<User> getUsers() {
        return users;
    }
    public User getUser(String string) {
        User result = null;
        for (User u : users) {
            if (u.getId().equals(string)) {
                result = u;
                break;
            }
        }
        return result;
    }
    public User getUser(UUID uuid) {
        return getUser(uuid.toString());
    }
    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    // methods
    public void saveUser(User user) {
        context.updateOrInsert("users", user);
    }
    public void save() {
        for (User u : users) {
            saveUser(u);
        }
    }
    public void load() {
        users.addAll(context.selectAll("SELECT * FROM users;", User.class));
    }
}
