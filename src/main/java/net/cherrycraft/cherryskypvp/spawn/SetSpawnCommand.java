package net.cherrycraft.cherryskypvp.spawn;

import net.cherrycraft.cherryskypvp.manager.CommandManager;
import net.cherrycraft.cherryskypvp.CherrySkyPVP;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends CommandManager {
    public SetSpawnCommand(String commandName) {
        super("setspawn");
        setPermission("reefrealmskypvp.setspawn");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        CherrySkyPVP.getInstance().getConfig().set("spawn.world", location.getWorld().getName());
        CherrySkyPVP.getInstance().getConfig().set("spawn.x", location.getX());
        CherrySkyPVP.getInstance().getConfig().set("spawn.y", location.getY());
        CherrySkyPVP.getInstance().getConfig().set("spawn.z", location.getZ());
        CherrySkyPVP.getInstance().getConfig().set("spawn.yaw", location.getYaw());
        CherrySkyPVP.getInstance().getConfig().set("spawn.pitch", location.getPitch());

        CherrySkyPVP.getInstance().saveConfig();
        player.sendMessage("Spawn location saved.");
    }
}
