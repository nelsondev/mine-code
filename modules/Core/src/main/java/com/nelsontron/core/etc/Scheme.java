package com.nelsontron.core.etc;

import com.nelsontron.core.util.BukkitUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class Scheme {

    public static ChatColor PRIMARY;
    public static ChatColor SECONDARY;
    public static ChatColor TEXT;
    public static ChatColor MUTED;
    public static ChatColor INFO;
    public static ChatColor WARN;
    public static ChatColor ERROR;

    public static void config(FileConfiguration config) {
        PRIMARY = BukkitUtil.getChatColor(config.getString("colors.primary"));
        SECONDARY = BukkitUtil.getChatColor(config.getString("colors.secondary"));
        TEXT = BukkitUtil.getChatColor(config.getString("colors.text"));
        MUTED = BukkitUtil.getChatColor(config.getString("colors.muted"));
        INFO = BukkitUtil.getChatColor(config.getString("colors.info"));
        WARN = BukkitUtil.getChatColor(config.getString("colors.warn"));
        ERROR = BukkitUtil.getChatColor(config.getString("colors.error"));
        validate(config);
    }

    private static void validate(FileConfiguration config) {
        if (PRIMARY == null) config.set("colors.primary", "white");
        if (SECONDARY == null) config.set("colors.secondary", "white");
        if (TEXT == null) config.set("colors.secondary", "white");
        if (MUTED == null) config.set("colors.muted", "white");
        if (INFO == null) config.set("colors.info", "white");
        if (WARN == null) config.set("colors.warn", "white");
        if (ERROR == null) config.set("colors.error", "white");
    }
}
