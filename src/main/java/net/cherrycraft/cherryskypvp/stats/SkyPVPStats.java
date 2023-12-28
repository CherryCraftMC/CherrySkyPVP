package net.cherrycraft.cherryskypvp.stats;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.cherrycraft.cherryskypvp.data.MySQL;
import net.cherrycraft.cherryskypvp.level.LevelSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SkyPVPStats implements Listener {
    private Map<UUID, Long> cooldown = new HashMap<>();
    private Map<UUID, DamagerInfo> lastDamager = new HashMap<>();
    private static final long COOLDOWN_TIME = 6000;
    private static final long DAMAGE_TIME = 10000; // 10 seconds in milliseconds

    MiniMessage miniMessage = MiniMessage.builder().build();
    LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

    private static class DamagerInfo {
        UUID damagerUUID;
        long timestamp;

        DamagerInfo(UUID damagerUUID, long timestamp) {
            this.damagerUUID = damagerUUID;
            this.timestamp = timestamp;
        }
    }

    private boolean isOnCooldown(Player player){
        UUID playerID = player.getUniqueId();
        return cooldown.containsKey(playerID) && System.currentTimeMillis() - cooldown.get(playerID)< COOLDOWN_TIME;
    }

    private void setCooldown(Player player){
        cooldown.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            lastDamager.put(player.getUniqueId(), new DamagerInfo(damager.getUniqueId(), System.currentTimeMillis()));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        DamagerInfo damagerInfo = lastDamager.get(player.getUniqueId());
        if (damagerInfo != null && System.currentTimeMillis() - damagerInfo.timestamp <= DAMAGE_TIME) {
            Player killer = Bukkit.getPlayer(damagerInfo.damagerUUID);
            if (killer != null && !killer.equals(player)){
                killer.setHealth(Math.min(killer.getHealth()+4, killer.getMaxHealth()));
                Component message = miniMessage.deserialize("<#C1260E>"+ player.getName()+"<#6A6177> has been killed by <#0E98C1>"+killer.getName()+"!");
                event.deathMessage(message);
                MySQL.incrementKills(killer.getUniqueId().toString());
                MySQL.incrementKillStreak(killer.getUniqueId().toString());
                MySQL.calculateKD(killer.getUniqueId().toString());
                MySQL.addCoins(killer.getUniqueId().toString(), 50);
                checkAndClaimBounty(player, killer);
                Random random = new Random();
                int randomInt = random.nextInt(150);
                Component xpMessage = miniMessage.deserialize("<#6A6177>[<#2BCD7E>■<#6A6177>] <#6A6177>You received:<#0E98C1> "+ "<hover:show_text:'<#6A6177>Reward for killing <#C1260E>"+player.getName()+"'>"+randomInt+"xp<#6A6177>!");
                killer.sendMessage(xpMessage);
                LevelSystem.addXp(killer, randomInt);
                if (MySQL.getKillStreak(killer.getUniqueId().toString()) % 5 == 0) {
                    Component killstreakMessage = miniMessage.deserialize("<#6A6177>"+killer.getName()+"<#6A6177> is on a <#0E98C1>"+MySQL.getKillStreak(killer.getUniqueId().toString())+"<gradient:#5e4fa2:#f79459><bold> KILLSTREAK</gradient><#6A6177>!");
                    Bukkit.broadcast(killstreakMessage);
                }
            }
        }
        MySQL.resetKillStreak(player.getUniqueId().toString());
        MySQL.incrementDeaths(player.getUniqueId().toString());
        MySQL.calculateKD(player.getUniqueId().toString());
        // Remove the player's UUID from the lastDamager map
        lastDamager.remove(player.getUniqueId());
    }


    private void checkAndClaimBounty(Player killedPlayer, Player killer) {
        String killedPlayerUUID = killedPlayer.getUniqueId().toString();
        int bounty = MySQL.getBounty(killedPlayerUUID);
        if (bounty > 0) {
            MySQL.addCoins(killer.getUniqueId().toString(), bounty);
            MySQL.removeBountyfromExistance(killedPlayerUUID);
        }
    }
}