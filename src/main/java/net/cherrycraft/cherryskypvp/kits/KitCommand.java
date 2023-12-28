package net.cherrycraft.cherryskypvp.kits;

import net.cherrycraft.cherryskypvp.Loader;
import net.cherrycraft.cherryskypvp.manager.CommandManager;
import net.cherrycraft.cherryskypvp.kits.system.KitsSystem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand extends CommandManager {
    public KitCommand(String commandName) {
        super("kit");
        setPermission("reefrealmskypvp.kit");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return;
        }

        if (args.length == 0) {
            sender.sendMessage("Available kits: " + String.join(", ", Loader.getKitManager().getKits()));
            return;
        }

        Player player = (Player) sender;
        KitsSystem kit = Loader.getKitManager().getKit(args[0]);

        if (kit == null) {
            sender.sendMessage("That kit does not exist.");
            return;
        }

        kit.equip(player);
        sender.sendMessage("You have been equipped with the " + kit.getName() + " kit.");
    }
}