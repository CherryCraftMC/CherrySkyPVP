package net.reefrealm.reefrealmskypvp.trading;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class TradeSystem implements Listener {

    @EventHandler
    public void ClickPlayerEvent(PlayerInteractEntityEvent event){
        if (event.getRightClicked() instanceof Player) {
            Player player = event.getPlayer();
            Player target = (Player) event.getRightClicked();
            if (player.getItemInHand().getType().equals(Material.EMERALD)) {
                player.sendMessage("You have clicked on " + target.getName());
                target.sendMessage(player.getName() + " has clicked on you");
            }
        }

    }


}
