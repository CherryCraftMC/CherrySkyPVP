package net.reefrealm.reefrealmskypvp.economy;

import net.reefrealm.reefrealmskypvp.data.MySQL;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CoinsSystem{

    public static void addCoins(Player player, int coins){
        MySQL.addCoins(player.getUniqueId().toString(), coins);
    }

    public static void overWriteCoins(Player player, int coins){
        MySQL.overrideCoinsAmount(player.getUniqueId().toString(), coins);
    }

    public static void removeCoins(Player player, int coins){
        MySQL.removeCoins(player.getUniqueId().toString(), coins);
    }


}