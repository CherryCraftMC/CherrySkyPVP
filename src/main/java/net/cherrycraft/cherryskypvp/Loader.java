package net.cherrycraft.cherryskypvp;

import net.cherrycraft.cherryskypvp.launcher.LaunchpadSystem;
import net.cherrycraft.cherryskypvp.manager.CommandManager;
import net.cherrycraft.cherryskypvp.spawn.AlwaysSpawn;
import net.cherrycraft.cherryskypvp.spawn.SetSpawnCommand;
import net.cherrycraft.cherryskypvp.stats.SkyPVPStats;
import net.cherrycraft.cherryskypvp.bounty.commands.BountyCommand;
import net.cherrycraft.cherryskypvp.economy.command.CoinsCommand;
import net.cherrycraft.cherryskypvp.kits.KitCommand;
import net.cherrycraft.cherryskypvp.kits.list.Jumper;
import net.cherrycraft.cherryskypvp.kits.system.KitManager;
import net.cherrycraft.cherryskypvp.placeholders.StatsPlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class Loader implements Listener {

    private static KitManager kitManager;


    public static void registerCommands(CherrySkyPVP plugin) {
        //registeredCommand(new LanguageCommand("language"), plugin);
        registeredCommand(new SetSpawnCommand("setspawn"), plugin);
        registeredCommand(new CoinsCommand("coins"), plugin);
        registeredCommand(new BountyCommand("bounty"), plugin);
        registeredCommand(new KitCommand("kit"), plugin);

    }


    private static void registeredCommand(CommandManager command, CherrySkyPVP plugin) {
        command.register(plugin);
        plugin.getLogger().info("Command '" + command.getCommandName() + "' has been registered.");
    }

    private static void registerKits(CherrySkyPVP plugin) {
        // Register kits here
        kitManager = new KitManager();
        kitManager.registerKit(new Jumper("Jumper", "Jumper kit"));
    }

    public static void registerListeners(CherrySkyPVP plugin) {
        // Register listeners here
        //Bukkit.getPluginManager().registerEvents(new ExampleListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new SkyPVPStats(),plugin);
        Bukkit.getPluginManager().registerEvents(new AlwaysSpawn() ,plugin);
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new StatsPlaceholderExpansion().register();
        }
        Bukkit.getPluginManager().registerEvents(new LaunchpadSystem(),plugin);

        // Register kits
        registerKits(plugin);
        // Register kit listeners
        Bukkit.getPluginManager().registerEvents(new Jumper("Jumper","Jumper Kit"), plugin);

    }

    public static KitManager getKitManager() {
        return kitManager;
    }
}