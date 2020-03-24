package com.nelsontron.warps;

import com.nelsontron.core.etc.Scheme;
import com.nelsontron.core.util.BukkitUtil;
import com.nelsontron.core.util.TableUtil;
import com.nelsontron.warps.entity.*;
import com.nelsontron.warps.executor.WarpExecutor;
import com.nelsontron.warps.listener.PlayerJoinListener;
import com.nelsontron.warps.listener.PlayerQuitListener;
import com.nelsontron.warps.provider.GlobalProvider;
import com.nelsontron.warps.provider.UserProvider;
import com.nelsontron.warps.util.DependencyLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static FileConfiguration sqlConfig;
    public static String sqlProvider;
    public static String sqlDataPath;
    public static String sqlDatabase;

    private UserProvider userProvider;
    private GlobalProvider globalProvider;

    static {
        DependencyLoader.check("Core", "0.0.3",
                "https://github.com/nelsondev/mine-code/releases/download/v0.0.3/Core.jar");
    }

    public Main() {
        // check dependencies
        sqlConfig = BukkitUtil.getSqliteConfig(this);
        sqlProvider = sqlConfig.getString("provider");
        sqlDataPath = sqlConfig.getString("dataPath");
        sqlDatabase = sqlConfig.getString("database");
    }

    @Override
    public void onEnable() {
        // data stuff
        createDefaultTables();

        SqliteContext context = new SqliteContext(sqlDatabase);

        userProvider = new UserProvider(context);
        userProvider.load();
        globalProvider = new GlobalProvider(context);
        globalProvider.load();

        // bukkit stuff
        saveDefaultConfig();
        Scheme.config(getConfig());

        // register events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(userProvider), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(userProvider), this);

        // register commands
        BukkitUtil.registerCommands(this, new WarpExecutor(userProvider, globalProvider), "warp", "w");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        userProvider.save();
        globalProvider.save();
    }

    // methods
    private void createDefaultTables() {
        String userTable = TableUtil.getTable("users", User.class);
        String warpTable = TableUtil.getTable("warps", Warp.class);

        SqliteContext db = new SqliteContext(sqlDatabase);

        db.query(userTable);
        db.query(warpTable);
    }
}
