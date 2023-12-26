package net.cherrycraft.cherryskypvp.manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

public abstract class CommandManager implements CommandExecutor {
    private String commandName;
    private String permission;
    private String permissionMessage;
    private String usage;
    private String description;

    public CommandManager(String commandName) {
        this.commandName = commandName;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public String getCommandName() {
        return commandName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    public void setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void register(Plugin plugin) {
        PluginCommand pluginCommand = plugin.getServer().getPluginCommand(getCommandName());
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        } else {
            plugin.getLogger().warning("Failed to register command: " + getCommandName());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (getPermission() != null && !sender.hasPermission(getPermission())) {
            sender.sendMessage(getPermissionMessage());
            return true;
        }

        execute(sender, args);
        return true;
    }
}
