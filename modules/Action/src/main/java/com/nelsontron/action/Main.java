package com.nelsontron.action;

import com.nelsontron.action.executor.ActionExecutor;
import com.nelsontron.action.provider.ActionProvider;
import com.nelsontron.action.util.BukkitUtil;
import com.nelsontron.action.util.DependencyLoader;
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

    private ActionProvider actionProvider;

    static {
        DependencyLoader.check("Core",
                "https://github.com/nelsondev/mine-code/releases/download/lib/Core.v0.0.1.jar");
    }

    public Main() {
        // check dependencies
        sqlConfig = getSqliteConfig();
        sqlProvider = sqlConfig.getString("provider");
        sqlDataPath = sqlConfig.getString("dataPath");
    }

    @Override
    public void onEnable() {
        // data stuff
        createDefaultTables();

        SqliteContext context = new SqliteContext(sqlDataPath);

        actionProvider = new ActionProvider(context);
        actionProvider.load();

        // register events


        // register commands
        BukkitUtil.registerCommands(this, new ActionExecutor(actionProvider), "action");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        actionProvider.save();
    }

    // methods
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

    public FileConfiguration getSqliteConfig() {
        InputStream stream = getResource("sqlite.yml");
        InputStreamReader reader;
        reader = new InputStreamReader(Objects.requireNonNull(stream));
        return YamlConfiguration.loadConfiguration(reader);
    }
}
