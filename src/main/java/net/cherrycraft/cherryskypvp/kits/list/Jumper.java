package net.cherrycraft.cherryskypvp.kits.list;

import net.cherrycraft.cherryskypvp.CherrySkyPVP;
import net.cherrycraft.cherryskypvp.kits.system.KitsSystem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Jumper extends KitsSystem implements Listener {
    private final HashMap<UUID, Boolean> canDoubleJump = new HashMap<>();

    public Jumper(String name, String description) {
        super("Jumper", "Jumper kit");
    }

    @Override
    public void equip(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        inventory.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
        inventory.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    if (player.getGameMode() == GameMode.SURVIVAL && player.isOnGround() && !canDoubleJump.getOrDefault(player.getUniqueId(), false)) {
        player.setAllowFlight(true);
        canDoubleJump.put(player.getUniqueId(), true);
    }
}

    @EventHandler
    public void onFlightAttempt(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL && canDoubleJump.getOrDefault(player.getUniqueId(), false)) {
            event.setCancelled(true);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));
            canDoubleJump.put(player.getUniqueId(), false);

            new BukkitRunnable() {
                @Override
                public void run() {
                    canDoubleJump.put(player.getUniqueId(), true);
                }
            }.runTaskLater(JavaPlugin.getPlugin(CherrySkyPVP.class), 100); // 100 ticks = 5 seconds
        }
    }
}