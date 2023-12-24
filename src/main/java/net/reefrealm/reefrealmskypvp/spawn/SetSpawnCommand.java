package net.reefrealm.reefrealmskypvp.spawn;

import net.reefrealm.reefrealmskypvp.ReefRealmSkyPVP;
import net.reefrealm.reefrealmskypvp.manager.CommandManager;
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

        ReefRealmSkyPVP.getInstance().getConfig().set("spawn.world", location.getWorld().getName());
        ReefRealmSkyPVP.getInstance().getConfig().set("spawn.x", location.getX());
        ReefRealmSkyPVP.getInstance().getConfig().set("spawn.y", location.getY());
        ReefRealmSkyPVP.getInstance().getConfig().set("spawn.z", location.getZ());
        ReefRealmSkyPVP.getInstance().getConfig().set("spawn.yaw", location.getYaw());
        ReefRealmSkyPVP.getInstance().getConfig().set("spawn.pitch", location.getPitch());

        ReefRealmSkyPVP.getInstance().saveConfig();
        player.sendMessage("Spawn location saved.");
    }
}
