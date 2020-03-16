package com.nelsontron.warps;

import com.nelsontron.warps.executor.WarpExecutor;
import com.nelsontron.warps.listener.PlayerJoinListener;
import com.nelsontron.warps.listener.PlayerQuitListener;
import com.nelsontron.warps.util.BukkitUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public final class Main extends JavaPlugin {

    public static FileConfiguration sqlConfig;
    public static String sqlProvider;
    public static String sqlDataPath;

    public static final ChatColor PRIMARY = ChatColor.BLUE;
    public static final ChatColor SECONDARY = ChatColor.AQUA;
    public static final ChatColor TEXT = ChatColor.WHITE;
    public static final ChatColor MUTED = ChatColor.DARK_GRAY;
    public static final ChatColor INFO = ChatColor.GRAY;
    public static final ChatColor WARN = ChatColor.GOLD;
    public static final ChatColor ERROR = ChatColor.RED;

    private UserProvider userProvider;

    public Main() {
        sqlConfig = getSqliteConfig();
        sqlProvider = sqlConfig.getString("provider");
        sqlDataPath = sqlConfig.getString("dataPath");
        userProvider = new UserProvider();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        createDefaultTables();
        createDefaultData();
        userProvider.load();

        // register events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(userProvider), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(userProvider), this);

        // register commands
        BukkitUtil.registerCommands(this, new WarpExecutor(userProvider), "warp", "w");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        userProvider.save();
    }

    private void createDefaultTables() {
        String createUserTable =
                "CREATE TABLE IF NOT EXISTS users ("
                        + "id text PRIMARY KEY"
                        + ");";
        String createWarpTable =
                "CREATE TABLE IF NOT EXISTS warps ("
                        + "id integer PRIMARY KEY,"
                        + "userId text REFERENCES users(id),"
                        + "name text,"
                        + "category text,"
                        + "location text"
                        + ");";

        SqliteContext db = new SqliteContext("warps.db");

        db.query(createUserTable);
        db.query(createWarpTable);
    }
    private void createDefaultData() {}

    public FileConfiguration getSqliteConfig() {
        InputStream stream = getResource("sqlite.yml");
        InputStreamReader reader;
        reader = new InputStreamReader(Objects.requireNonNull(stream));
        return YamlConfiguration.loadConfiguration(reader);
    }
}
