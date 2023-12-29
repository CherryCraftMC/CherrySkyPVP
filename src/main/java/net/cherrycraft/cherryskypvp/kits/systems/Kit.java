package net.cherrycraft.cherryskypvp.kits.systems;

import java.util.List;

public abstract class Kit {
    private final String name;
    private final String description;

    public Kit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    List<String> getItems() {

        return null;
    }
}
