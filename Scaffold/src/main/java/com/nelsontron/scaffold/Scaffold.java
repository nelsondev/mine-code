package com.nelsontron.scaffold;

import com.nelsontron.core.util.BukkitUtil;
import com.nelsontron.core.util.TableUtil;
import com.nelsontron.scaffold.entity.SqliteContext;
import com.nelsontron.scaffold.entity.User;
import com.nelsontron.scaffold.provider.UserProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Scaffold extends JavaPlugin {

    public static FileConfiguration sqlConfig;
    public static String sqlProvider;
    public static String sqlDataPath;
    public static String sqlDatabase;

    private UserProvider userProvider;

    public Scaffold() {
        sqlConfig = BukkitUtil.getSqliteConfig(this);
        sqlProvider = sqlConfig.getString("provider");
        sqlDataPath = sqlConfig.getString("dataPath");
        sqlDatabase = sqlConfig.getString("database");
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        createDefaultTables();

        SqliteContext context = new SqliteContext(sqlDatabase);
        userProvider = new UserProvider(context);
        userProvider.load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        userProvider.save();
    }

    // methods
    private void createDefaultTables() {
        String userTable = TableUtil.getTable("users", User.class);

        SqliteContext db = new SqliteContext(sqlDatabase);

        db.query(userTable);
    }
}
