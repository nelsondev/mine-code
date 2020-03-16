package com.nelsontron.kits;

import com.nelsontron.kits.executor.KitExecutor;
import com.nelsontron.kits.listener.PlayerJoinListener;
import com.nelsontron.kits.listener.PlayerQuitListener;
import com.nelsontron.kits.util.BukkitUtil;
import com.nelsontron.kits.util.DependencyLoader;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        // check dependencies
        sqlConfig = getSqliteConfig();
        sqlProvider = sqlConfig.getString("provider");
        sqlDataPath = sqlConfig.getString("dataPath");
    }

    @Override
    public void onEnable() {
        // dependencies
        DependencyLoader.checkDependency("Core", "https://github.com/nelsondev/mine-code/releases/download/lib/Core.jar");

        // data
        createDefaultTables();
        createDefaultData();
        userProvider = new UserProvider();
        userProvider.load();

        // register events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(userProvider), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(userProvider), this);

        // register commands
        BukkitUtil.registerCommands(this, new KitExecutor(userProvider), "kit");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        userProvider.save();
    }

    // methods
    private void createDefaultTables() {
        String createUserTable =
                "CREATE TABLE IF NOT EXISTS users ("
                        + "id text PRIMARY KEY"
                        + ");";
        String createKitTable =
                "CREATE TABLE IF NOT EXISTS kits ("
                        + "id integer PRIMARY KEY,"
                        + "userId text REFERENCES users(id),"
                        + "name text"
                        + ");";
        String createItemTable =
                "CREATE TABLE IF NOT EXISTS items ("
                        + "id integer PRIMARY KEY,"
                        + "kitId integer REFERENCES kits(id),"
                        + "name text,"
                        + "desc text,"
                        + "category text,"
                        + "type text,"
                        + "amount int,"
                        + "price int"
                        + ");";
        String createEnchantTable =
                "CREATE TABLE IF NOT EXISTS enchantments ("
                        + "    id integer PRIMARY KEY,"
                        + "    itemId integer REFERENCES items(id),"
                        + "    name text,"
                        + "    power int"
                        + ");";

        SqliteContext db = new SqliteContext("kits.db");

        db.query(createUserTable);
        db.query(createKitTable);
        db.query(createItemTable);
        db.query(createEnchantTable);
    }
    private void createDefaultData() {}

    public FileConfiguration getSqliteConfig() {
        InputStream stream = getResource("sqlite.yml");
        InputStreamReader reader;
        reader = new InputStreamReader(Objects.requireNonNull(stream));
        return YamlConfiguration.loadConfiguration(reader);
    }
}
