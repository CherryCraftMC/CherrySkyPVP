package net.cherrycraft.cherryskypvp.economy;

import net.cherrycraft.cherryskypvp.data.MySQL;
import org.bukkit.entity.Player;

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