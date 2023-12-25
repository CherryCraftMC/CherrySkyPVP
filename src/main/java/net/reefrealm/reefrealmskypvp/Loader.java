package net.reefrealm.reefrealmskypvp;

import net.reefrealm.reefrealmskypvp.economy.command.CoinsCommand;
import net.reefrealm.reefrealmskypvp.launcher.LaunchpadSystem;
import net.reefrealm.reefrealmskypvp.manager.CommandManager;
import net.reefrealm.reefrealmskypvp.placeholders.StatsPlaceholderExpansion;
import net.reefrealm.reefrealmskypvp.spawn.AlwaysSpawn;
import net.reefrealm.reefrealmskypvp.spawn.SetSpawnCommand;
import net.reefrealm.reefrealmskypvp.stats.SkyPVPStats;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class Loader implements Listener {


    public static void registerCommands(ReefRealmSkyPVP plugin) {
        //registeredCommand(new LanguageCommand("language"), plugin);
        registeredCommand(new SetSpawnCommand("setspawn"), plugin);
        registeredCommand(new CoinsCommand("coins"), plugin);
    }


    private static void registeredCommand(CommandManager command, ReefRealmSkyPVP plugin) {
        command.register(plugin);
        plugin.getLogger().info("Command '" + command.getCommandName() + "' has been registered.");
    }

    public static void registerListeners(ReefRealmSkyPVP plugin) {
        // Register listeners here
        //Bukkit.getPluginManager().registerEvents(new ExampleListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new SkyPVPStats(),plugin);
        Bukkit.getPluginManager().registerEvents(new AlwaysSpawn() ,plugin);
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new StatsPlaceholderExpansion().register();
        }
        Bukkit.getPluginManager().registerEvents(new LaunchpadSystem(),plugin);

    }
}