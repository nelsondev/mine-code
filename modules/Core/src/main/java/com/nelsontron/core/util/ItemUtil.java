package com.nelsontron.core.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {
    public static String serialize(ItemStack item) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", item);
        return config.saveToString();
    }
    public static ItemStack deserialize(String yml) {
        ItemStack result = null;
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(yml);
            result = config.getItemStack("item");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return result;
    }
}
