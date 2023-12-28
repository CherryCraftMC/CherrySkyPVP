package net.cherrycraft.cherryskypvp.kits.system;

import org.bukkit.entity.Player;

public abstract class KitsSystem {
    protected String name;
    protected String description;

    public KitsSystem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public abstract void equip(Player player);
}
