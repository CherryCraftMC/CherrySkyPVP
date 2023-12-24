package net.reefrealm.reefrealmskypvp.spawn;

import net.reefrealm.reefrealmskypvp.ReefRealmSkyPVP;
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
        String worldName = ReefRealmSkyPVP.getInstance().getConfig().getString("spawn.world");
        double x = ReefRealmSkyPVP.getInstance().getConfig().getDouble("spawn.x");
        double y = ReefRealmSkyPVP.getInstance().getConfig().getDouble("spawn.y");
        double z = ReefRealmSkyPVP.getInstance().getConfig().getDouble("spawn.z");
        float yaw = (float) ReefRealmSkyPVP.getInstance().getConfig().getDouble("spawn.yaw");
        float pitch = (float) ReefRealmSkyPVP.getInstance().getConfig().getDouble("spawn.pitch");

        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            Location spawnLocation = new Location(world, x, y, z, yaw, pitch);
            player.teleport(spawnLocation);
        }
    }
}