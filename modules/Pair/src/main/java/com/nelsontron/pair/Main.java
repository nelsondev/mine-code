package com.nelsontron.pair;

import com.nelsontron.core.etc.Scheme;
import com.nelsontron.core.util.BukkitUtil;
import com.nelsontron.core.util.TableUtil;
import com.nelsontron.pair.entity.Gamer;
import com.nelsontron.pair.entity.Pair;
import com.nelsontron.pair.entity.SqliteContext;
import com.nelsontron.pair.executor.PairExecutor;
import com.nelsontron.pair.listener.PlayerJoinListener;
import com.nelsontron.pair.listener.PlayerQuitListener;
import com.nelsontron.pair.provider.GamerProvider;
import com.nelsontron.pair.provider.PairProvider;
import com.nelsontron.pair.util.DependencyLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static FileConfiguration sqlConfig;
    public static String sqlProvider;
    public static String sqlDataPath;
    public static String sqlDatabase;

    private GamerProvider gamerProvider;

    static {
        DependencyLoader.check("Core", "0.0.3",
                "https://github.com/nelsondev/mine-code/releases/download/v0.0.3/Core.jar");
    }

    public Main() {
        sqlConfig = BukkitUtil.getSqliteConfig(this);
        sqlProvider = sqlConfig.getString("provider");
        sqlDataPath = sqlConfig.getString("dataPath");
        sqlDatabase = sqlConfig.getString("database");
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        createDefaultTables();

        SqliteContext context = new SqliteContext(sqlDatabase);

        gamerProvider = new GamerProvider(context);
        gamerProvider.load();
        PairProvider pairProvider = new PairProvider(context);

        Scheme.config(getConfig());

        // commands
        BukkitUtil.registerCommands(this, new PairExecutor(this, pairProvider), "pair");

        // listener
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(gamerProvider), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(gamerProvider), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        gamerProvider.save();
    }

    private void createDefaultTables() {
        String gamersTable = TableUtil.getTable(GamerProvider.table, Gamer.class);
        String pairsTable = TableUtil.getTable(PairProvider.table, Pair.class);

        SqliteContext db = new SqliteContext(sqlDatabase);

        db.query(gamersTable);
        db.query(pairsTable);
    }
}
