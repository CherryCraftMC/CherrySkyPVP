package net.reefrealm.reefrealmskypvp.launcher;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class LaunchpadSystem implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
            if (event.getClickedBlock().getRelative(0, -1, 0).getType() == Material.SLIME_BLOCK) {
                Player player = event.getPlayer();
                Vector direction = player.getEyeLocation().getDirection();
                player.setVelocity(direction.multiply(2)); // Adjust the multiplier to control the launch speed
            }
        }
    }
}
