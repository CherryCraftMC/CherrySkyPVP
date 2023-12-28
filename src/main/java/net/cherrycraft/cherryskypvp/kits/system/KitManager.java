package net.cherrycraft.cherryskypvp.kits.system;

import java.util.HashMap;
import java.util.Set;

public class KitManager {
    private HashMap<String, KitsSystem> kits = new HashMap<>();

    public void registerKit(KitsSystem kit) {
        kits.put(kit.getName(), kit);
    }

    public KitsSystem getKit(String name) {
        return kits.get(name);
    }

    public Set<String> getKits() {
        return kits.keySet();
    }
}