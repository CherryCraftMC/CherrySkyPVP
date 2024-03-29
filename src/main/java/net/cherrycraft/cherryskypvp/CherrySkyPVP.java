package net.cherrycraft.cherryskypvp;

import net.cherrycraft.cherryskypvp.data.MySQL;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CherrySkyPVP extends JavaPlugin {

    static CherrySkyPVP instance;

    private final YamlConfiguration conf = new YamlConfiguration();
    Logger logger = Logger.getLogger("CherrySkyPVP");

    public static CherrySkyPVP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        loadLoader();
        loadDatabase();
        logger.info("SkyPVP Plugin loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    void loadDatabase() {
        String host = getConfig().getString("database.host");
        int port = getConfig().getInt("database.port");
        String database = getConfig().getString("database.database");
        String user = getConfig().getString("database.username");
        String password = getConfig().getString("database.password");

        MySQL.Login(host, database, user, password, port);
        MySQL.createTables();
    }

    private void loadLoader(){
        Loader.registerCommands(this);
        Loader.registerListeners(this);
    }


}
