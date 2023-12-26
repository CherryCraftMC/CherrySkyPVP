package net.cherrycraft.cherryskypvp.spawn;

import net.cherrycraft.cherryskypvp.CherrySkyPVP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class AlwaysSpawn implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        teleportToSpawn(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        teleportToSpawn(event.getPlayer());
    }

    private void teleportToSpawn(Player player) {
        String worldName = CherrySkyPVP.getInstance().getConfig().getString("spawn.world");
        double x = CherrySkyPVP.getInstance().getConfig().getDouble("spawn.x");
        double y = CherrySkyPVP.getInstance().getConfig().getDouble("spawn.y");
        double z = CherrySkyPVP.getInstance().getConfig().getDouble("spawn.z");
        float yaw = (float) CherrySkyPVP.getInstance().getConfig().getDouble("spawn.yaw");
        float pitch = (float) CherrySkyPVP.getInstance().getConfig().getDouble("spawn.pitch");

        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            Location spawnLocation = new Location(world, x, y, z, yaw, pitch);
            player.teleport(spawnLocation);
        }
    }
}