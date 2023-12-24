package net.reefrealm.reefrealmskypvp.level;

import net.reefrealm.reefrealmskypvp.data.MySQL;
import org.bukkit.entity.Player;

public class LevelSystem {

    public static void addXp(Player player, int xp) {
        String playerUUID = player.getUniqueId().toString();
        int currentScore = MySQL.getScore(playerUUID);
        int currentLevel = MySQL.getLevel(playerUUID);
        int newScore = currentScore + xp;

        // Check if the player has enough score to level up
        while (newScore >= getExperienceForLevel(currentLevel + 1)) {
            newScore -= getExperienceForLevel(currentLevel + 1);
            currentLevel++;
            player.sendMessage("You leveled up to level " + currentLevel + "!");
        }

        // Update the score and level in the database
        MySQL.setScore(playerUUID, newScore);
        MySQL.setLevel(playerUUID, currentLevel);
    }

    public int getLevel(Player player) {
        return MySQL.getLevel(player.getUniqueId().toString());
    }

    public static float getProgress(Player player) {
        String playerUUID = player.getUniqueId().toString();
        int currentScore = MySQL.getScore(playerUUID);
        int currentLevel = MySQL.getLevel(playerUUID);
        int nextLevelXp = getExperienceForLevel(currentLevel + 1);

        return (float) currentScore / nextLevelXp * 100.0f;
    }

    public static int getExperienceForLevel(int level) {
        return 1250 * (int)Math.pow(level, 2) + 6250 * level - 7500;
    }
}