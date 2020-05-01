package com.nelsontron.pair.executor;

import com.nelsontron.core.etc.Scheme;
import com.nelsontron.core.util.BukkitUtil;
import com.nelsontron.pair.Main;
import com.nelsontron.pair.entity.Pair;
import com.nelsontron.pair.game.Game;
import com.nelsontron.pair.provider.PairProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PairExecutor implements CommandExecutor {

    private Main main;
    private PairProvider pairs;
    private Game game;

    public PairExecutor(Main main, PairProvider pairs) {
        this.main = main;
        this.pairs = pairs;
        this.game = null;
    }

    // methods
    private boolean sendNotPlayer(CommandSender sender) {
        sender.sendMessage(Scheme.ERROR + "Sender not player.");
        return true;
    }
    private boolean sendHelp(CommandSender sender) {
        String prefix = Scheme.MUTED + "  /pair ";
        sender.sendMessage(Scheme.INFO + "Pair Help:");
        sender.sendMessage(prefix + "<player>" + Scheme.INFO + " - pair with a player.");
        sender.sendMessage(prefix + "create" + Scheme.INFO + " - create a new pair game.");
        sender.sendMessage(prefix + "generate" + Scheme.INFO + " - generate new pair world.");
        sender.sendMessage(prefix + "list" + Scheme.INFO + " - list current pairs.");
        sender.sendMessage(prefix + "state" + Scheme.INFO + " - show current pair state.");
        sender.sendMessage(prefix + "start" + Scheme.INFO + " - start current pair game.");
        sender.sendMessage(prefix + "stop" + Scheme.INFO + " - stop current pair game.");
        return true;
    }
    private boolean sendGameExists(CommandSender sender) {
        sender.sendMessage(Scheme.ERROR + "Game already exists.");
        return true;
    }
    private boolean sendGameNotExists(CommandSender sender) {
        sender.sendMessage(Scheme.ERROR + "No game exists.");
        return true;
    }
    private boolean sendPlayerNotExist(CommandSender sender) {
        sender.sendMessage(Scheme.ERROR + "Player doesn't exist.");
        return true;
    }
    private boolean sendUnableToPerform(CommandSender sender) {
        sender.sendMessage(Scheme.ERROR + "Unable to perform that command at this time.");
        return true;
    }

    private boolean createGame(CommandSender sender) {
        if (game != null) return sendGameExists(sender);

        sender.sendMessage(Scheme.INFO + "Creating game...");

        game = new Game(main, pairs);
        game.setup();

        sender.sendMessage(Scheme.INFO + "Game created!");

        return true;
    }
    private boolean generateGame(CommandSender sender) {
        if (game == null) return sendGameNotExists(sender);
        if (!game.getState().equals("setup")
            && !game.getState().equals("ready")) return sendUnableToPerform(sender);

        sender.sendMessage(Scheme.INFO + "Generating game...");

        game.generate();

        sender.sendMessage(Scheme.INFO + "Game generated!");

        return true;
    }
    private boolean startGame(CommandSender sender) {
        if (game == null) return sendGameNotExists(sender);
        if (!game.getState().equals("ready")) return sendUnableToPerform(sender);

        sender.sendMessage(Scheme.INFO + "Starting game...");

        game.start();

        sender.sendMessage(Scheme.INFO + "Game started!");

        return true;
    }
    private boolean joinGame(CommandSender sender, String playerName) {
        if (!(sender instanceof Player)) return sendNotPlayer(sender);
        if (playerName == null) return sendPlayerNotExist(sender);
        if (game == null) return sendGameNotExists(sender);
        if (!game.getState().equals("ready")) return sendUnableToPerform(sender);
        Player player1 = (Player) sender;
        Player player2 = BukkitUtil.getPlayer(playerName);
        Pair pair1 = game.getPair(player1);
        Pair pair2 = game.getPair(player2);

        if (pair2 != null && pair2.getPlayerUuid2().equals(player1.getName())) {
            pair2.setPlayer2(player1.getUniqueId().toString());

            player1.sendMessage(Scheme.INFO + "Accepted pairing request with " + player2.getName() + ".");
            player2.sendMessage(Scheme.INFO + player1.getName() + " has accepted your pairing request.");

            return true;
        }

        if (pair1 == null) {
            game.addRequestedPair(player1, player2.getName());

            player1.sendMessage(Scheme.INFO + "Requested to pair with " + player2.getName() + ".");
            player2.sendMessage(Scheme.INFO + "Request to pair sent by " + player1.getName() + ". Type /pair "
                    + player1.getName() + " to accept.");
            return true;
        }

        if (pair1.getPlayer2() == null) {
            game.removePair(pair1);

            player1.sendMessage(Scheme.INFO + "Overwrote old pairing request.");
            player1.performCommand("pair " + playerName);
            return true;
        }

        return true;
    }
    private boolean sendPairs(CommandSender sender) {
        for (Pair pair : pairs.getPlaying()) {
            sender.sendMessage(pair.toString());
        }
        sender.sendMessage(pairs.getPlaying().size() + " pairs playing.");

        return true;
    }
    private boolean stopGame(CommandSender sender) {
        if (game == null) return sendGameNotExists(sender);
        if (game.getState().equals("idle")) return sendUnableToPerform(sender);

        sender.sendMessage(Scheme.INFO + "Stopping game...");

        game.stop();
        game = null;

        sender.sendMessage(Scheme.INFO + "Game stopped!");

        return true;
    }
    private boolean sendState(CommandSender sender) {
        if (game == null) return sendGameNotExists(sender);

        sender.sendMessage("Current game state: " + game.getState());

        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) return sendHelp(sender);
        if (args[0].equalsIgnoreCase("create")) return createGame(sender);
        if (args[0].equalsIgnoreCase("generate")) return generateGame(sender);
        if (args[0].equalsIgnoreCase("start")) return startGame(sender);
        if (args[0].equalsIgnoreCase("stop")) return stopGame(sender);
        if (args[0].equalsIgnoreCase("list")) return sendPairs(sender);
        if (args[0].equalsIgnoreCase("state")) return sendState(sender);

        return joinGame(sender, args[0]);
    }
}
