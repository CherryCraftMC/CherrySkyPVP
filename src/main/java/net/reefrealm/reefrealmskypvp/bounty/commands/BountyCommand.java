package net.reefrealm.reefrealmskypvp.bounty.commands;

import net.reefrealm.reefrealmskypvp.data.MySQL;
import net.reefrealm.reefrealmskypvp.manager.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BountyCommand extends CommandManager implements TabCompleter {
    public BountyCommand(String commandName) {
        super("bounty");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // /bounty
            int bounty = MySQL.getBounty(player.getUniqueId().toString());
            player.sendMessage("You have a bounty of " + bounty + ".");
        } else {
            switch (args[0].toLowerCase()) {
                case "remove":
                    if (args.length == 2) {
                        if (!player.hasPermission("reefrealmskypvp.bounty.remove")) {
                            player.sendMessage("You do not have permission to use this command.");
                            return;
                        }
                        // /bounty remove <player>
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("That player is not online.");
                            return;
                        }
                        if (!MySQL.checkifBountyExist(target.getUniqueId().toString())) {
                            player.sendMessage("That player does not have a bounty.");
                            return;
                        }
                        MySQL.removeBountyfromExistance(target.getUniqueId().toString());
                        player.sendMessage("Removed " + target.getName() + "'s bounty.");
                    } else {
                        player.sendMessage("Invalid command. Use /bounty, /bounty add <player> <value>, /bounty remove <player>, or /bounty set <player> <value>.");
                    }
                    break;
                case "set":
                    if (args.length == 3) {
                        if (!player.hasPermission("reefrealmskypvp.bounty.set")) {
                            player.sendMessage("You do not have permission to use this command.");
                            return;
                        }
                        // /bounty set <player> <value>
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            player.sendMessage("That player is not online.");
                            return;
                        }
                        if (player.equals(target)) {
                            player.sendMessage("You cannot set a bounty on yourself.");
                            return;
                        }

                        int value;
                        try {
                            value = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("Invalid number.");
                            return;
                        }
                        if (MySQL.getCoins(player.getUniqueId().toString()) < value) {
                            player.sendMessage("You do not have enough coins to set that bounty.");
                            return;
                        }
                        if (MySQL.checkifBountyExist(target.getUniqueId().toString())) {
                            MySQL.setBounty(target.getUniqueId().toString(), value);
                            player.sendMessage("Set " + target.getName() + "'s bounty to " + value + ".");
                        } else {
                            MySQL.createBounty(target.getUniqueId().toString(), value);
                            player.sendMessage("Created " + target.getName() + "'s bounty to " + value + ".");
                        }
                        MySQL.removeCoins(player.getUniqueId().toString(), value);
                    } else {
                        player.sendMessage("Invalid command. Use /bounty, /bounty add <player> <value>, /bounty remove <player>, or /bounty set <player> <value>.");
                    }
                    break;
                default:
                    player.sendMessage("Invalid command. Use /bounty, /bounty add <player> <value>, /bounty remove <player>, or /bounty set <player> <value>.");
                    break;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            // If the player has only typed "/bounty ", suggest the subcommands
            List<String> suggestions = new ArrayList<>();
            if (sender.hasPermission("reefrealmskypvp.bounty.remove")) {
                suggestions.add("remove");
            }
            if (sender.hasPermission("reefrealmskypvp.bounty.set")) {
                suggestions.add("set");
            }
            return suggestions;
        } else if (args.length == 2) {
            // If the player has typed "/bounty <subcommand> ", suggest online player names
            if ((args[0].equalsIgnoreCase("remove") && sender.hasPermission("reefrealmskypvp.bounty.remove")) ||
                    (args[0].equalsIgnoreCase("set") && sender.hasPermission("reefrealmskypvp.bounty.set"))) {
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