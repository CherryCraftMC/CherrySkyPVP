package net.cherrycraft.cherryskypvp.economy.command;

import net.cherrycraft.cherryskypvp.data.MySQL;
import net.cherrycraft.cherryskypvp.economy.CoinsSystem;
import net.cherrycraft.cherryskypvp.manager.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CoinsCommand extends CommandManager implements TabCompleter {
    public CoinsCommand(String commandName) {
        super("coins");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // /coins
            int coins = MySQL.getCoins(player.getUniqueId().toString());
            player.sendMessage("You have " + coins + " coins.");
        } else {
            switch (args[0].toLowerCase()) {
                case "remove":
                    if (args.length == 3) {
                        if (!player.hasPermission("reefrealmskypvp.coins.remove")) {
                            player.sendMessage("You do not have permission to use this command.");
                            return;
                        }
                        // /coins remove <player> <value>
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("That player is not online.");
                            return;
                        }

                        int value;
                        try {
                            value = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("Invalid number.");
                            return;
                        }

                        CoinsSystem.removeCoins(target, value);
                        player.sendMessage("Removed " + value + " coins from " + target.getName() + ".");
                    } else {
                        player.sendMessage("Invalid usage. Use /coins remove <player> <value>.");
                    }
                    break;
                case "set":
                    if (args.length == 3) {
                        if (!player.hasPermission("reefrealmskypvp.coins.set")) {
                            player.sendMessage("You do not have permission to use this command.");
                            return;
                        }
                        // /coins set <player> <value>
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("That player is not online.");
                            return;
                        }

                        int value;
                        try {
                            value = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("Invalid number.");
                            return;
                        }

                        CoinsSystem.overWriteCoins(target, value);
                        player.sendMessage("Set " + target.getName() + "'s coins to " + value + ".");
                    } else {
                        player.sendMessage("Invalid usage. Use /coins set <player> <value>.");
                    }
                    break;
                case "add":
                    if (args.length == 3) {
                        if (!player.hasPermission("reefrealmskypvp.coins.add")) {
                            player.sendMessage("You do not have permission to use this command.");
                            return;
                        }
                        // /coins add <player> <value>
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("That player is not online.");
                            return;
                        }

                        int value;
                        try {
                            value = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("Invalid number.");
                            return;
                        }

                        CoinsSystem.addCoins(target, value);
                        player.sendMessage("Added " + value + " coins to " + target.getName() + ".");
                    } else {
                        player.sendMessage("Invalid usage. Use /coins add <player> <value>.");
                    }
                    break;
                default:
                    player.sendMessage("Invalid command. Use /coins, /coins remove <player> <value>, /coins set <player> <value>, or /coins add <player> <value>.");
                    break;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            // If the player has only typed "/coins ", suggest the subcommands
            List<String> suggestions = new ArrayList<>();
            if (sender.hasPermission("reefrealmskypvp.coins.add")) {
                suggestions.add("add");
            }
            if (sender.hasPermission("reefrealmskypvp.coins.remove")) {
                suggestions.add("remove");
            }
            if (sender.hasPermission("reefrealmskypvp.coins.set")) {
                suggestions.add("set");
            }
            return suggestions;
        } else if (args.length == 2) {
            // If the player has typed "/coins <subcommand> ", suggest online player names
            if ((args[0].equalsIgnoreCase("add") && sender.hasPermission("reefrealmskypvp.coins.add")) ||
                    (args[0].equalsIgnoreCase("remove") && sender.hasPermission("reefrealmskypvp.coins.remove")) ||
                    (args[0].equalsIgnoreCase("set") && sender.hasPermission("reefrealmskypvp.coins.set"))) {
                List<String> playerNames = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    playerNames.add(player.getName());
                }
                return playerNames;
            }
        }
        // If the player has typed anything else, don't suggest anything
        return null;
    }

}
