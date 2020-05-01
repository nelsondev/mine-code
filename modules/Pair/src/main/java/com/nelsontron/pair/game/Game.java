package com.nelsontron.pair.game;

import com.nelsontron.core.util.BukkitUtil;
import com.nelsontron.pair.Main;
import com.nelsontron.pair.entity.Pair;
import com.nelsontron.pair.provider.PairProvider;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Game {
    private final Main main;
    private final PairProvider pairs;
    private String state;
    private String worldName;
    private String lobbyWorldName;
    private int sizePerPair;
    private int chestMultiplier;

    public Game(Main main, PairProvider pairs) {
        this.main = main;
        this.pairs = pairs;
        this.state = "idle";
        this.worldName = main.getConfig().getString("world-name");
        this.lobbyWorldName = main.getConfig().getString("lobby-world-name");
        this.sizePerPair = main.getConfig().getInt("size-per-pair");
        this.chestMultiplier = main.getConfig().getInt("chest-multiplier");
    }

    // getters
    public Pair getPair(String uuid) {
        return pairs.getPair(uuid);
    }
    public Pair getPair(Player player) {
        return getPair(player.getUniqueId().toString());
    }
    public String getState() {
        return state;
    }

    // setters
    public void addRequestedPair(Player player1, String player2) {
        pairs.getList().add(new Pair(player1.getUniqueId().toString(), player2));
    }
    public void addPair(Player player1) {
        pairs.getList().add(new Pair(player1.getUniqueId().toString(), null));
    }
    public void addPair(Player player1, Player player2) {
        pairs.getList().add(new Pair(player1.getUniqueId().toString(), player2.getUniqueId().toString()));
    }
    public void setState(String state) {
        this.state = state;
    }

    public void removePair(Pair pair) {
        pairs.getList().remove(pair);
    }

    // methods
    public Location randomLocation() {
        Location result = null;
        World world = Bukkit.getWorld(worldName);
        int size = ((pairs.getPlaying().size() * sizePerPair) / 2) - 5;
        int x;
        int z;

        if (world != null) {
            x = BukkitUtil.randInt(-size, size) + BukkitUtil.randInt(-5, 5);
            z = BukkitUtil.randInt(-size, size) + BukkitUtil.randInt(-5, 5);

            result = new Location(world, x, world.getHighestBlockYAt(x, z) + 1, z);
        }
        return result;
    }
    public void teleportPairs(Location location) {
        for (Pair pair : pairs.getList()) {
            Player player1 = pair.getPlayer1();
            Player player2 = pair.getPlayer2();

            if (player1 != null && player2 != null) {
                player1.teleport(location);
                player2.teleport(location);
            }
        }
    }
    public void teleportPlayingPairs(Location location) {
        for (Pair pair : pairs.getPlaying()) {
            Player player1 = pair.getPlayer1();
            Player player2 = pair.getPlayer2();

            if (player1 != null && player2 != null) {
                player1.teleport(location);
                player2.teleport(location);
            }
        }
    }
    public void teleportPlayingPairsRandomly() {
        for (Pair pair : pairs.getPlaying()) {
            Player player1 = pair.getPlayer1();
            Player player2 = pair.getPlayer2();
            Location location = randomLocation();

            if (player1 != null && player2 != null) {
                player1.teleport(location);
                player2.teleport(location);
            }
        }
    }
    public void teleportPlayersToLobby() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(Bukkit.getWorld(lobbyWorldName).getSpawnLocation());
        }
    }

    public void createChests() {
        World world = Bukkit.getWorld(worldName);

        if (world != null) {

            int amount = pairs.getPlaying().size() * chestMultiplier;

            for (int i = 0; i < amount; i++) {
                Location location = randomLocation();
                world.getBlockAt(location).setType(Material.CHEST);
            }
        }
    }

    public void removeWorld() {
        teleportPlayersToLobby();
    }
    public void deleteWorld() {
        Bukkit.unloadWorld(worldName, false);
        try {
            FileUtils.deleteDirectory(new File(worldName));
        } catch (IOException ignored) { }
    }
    public void createWorld() {
        WorldCreator worldGenerator = new WorldCreator(worldName);

        worldGenerator.environment(World.Environment.NORMAL);
        worldGenerator.hardcore(true);
        worldGenerator.type(WorldType.NORMAL);

        World world = worldGenerator.createWorld();

        world.setSpawnLocation(0, world.getHighestBlockYAt(0, 0) + 1, 0);
        world.setHardcore(true);
        world.setPVP(true);
        world.setTime(0);
        world.setAutoSave(false);
    }
    public void createBorder() {
        World world = Bukkit.getWorld(worldName);
        int size = pairs.getPlaying().size() * sizePerPair;
        if (world != null) {
            WorldBorder border = world.getWorldBorder();

            border.setCenter(0, 0);
            border.setSize(size);
            main.getLogger().info("World border set to: " + size);
        }
    }

    public void setup() {
        if (state.equals("idle")) {
            state = "setting up";

            pairs.getList().clear();
            pairs.save();

            if (Bukkit.getWorld(worldName) != null)
                state = "ready";
            else
                state = "setup";
        }
    }
    public void generate() {
        if (state.equalsIgnoreCase("setup") || state.equalsIgnoreCase("ready")) {
            state = "generating";

            main.getLogger().info("Removing world...");
            removeWorld();
            main.getLogger().info("Deleting world...");
            deleteWorld();
            main.getLogger().info("Creating world...");
            createWorld();

            state = "ready";
        }
    }
    public void start() {
        if (state.equals("ready")) {
            state = "starting";

            main.getLogger().info("Creating border...");
            createBorder();
            main.getLogger().info("Creating chests...");
            createChests();

            main.getLogger().info("Teleporting pairs...");
            teleportPlayingPairsRandomly();

            state = "playing";
        }
    }
    public void stop() {
        if (!state.equals("idle")) {
            state = "stopping";

            teleportPlayersToLobby();

            state = "stopped";
        }
    }
}
