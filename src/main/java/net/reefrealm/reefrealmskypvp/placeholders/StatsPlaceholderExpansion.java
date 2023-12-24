package net.reefrealm.reefrealmskypvp.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.reefrealm.reefrealmskypvp.data.MySQL;
import net.reefrealm.reefrealmskypvp.level.LevelSystem;
import org.bukkit.entity.Player;

public class StatsPlaceholderExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "reefrealmskypvp";
    }

    @Override
    public String getAuthor() {
        return "MathiasClariDrenik";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        switch (identifier) {
            case "kills":
                return String.valueOf(MySQL.getKills(player.getUniqueId().toString()));
            case "deaths":
                return String.valueOf(MySQL.getDeaths(player.getUniqueId().toString()));
            case "kd":
                float kd = MySQL.getKD(player.getUniqueId().toString());
                return String.format("%.2f", kd);
            case "progress":
                float progress = LevelSystem.getProgress(player);
                return createProgressBar(progress);
            case "level":
                int level = MySQL.getLevel(player.getUniqueId().toString());
                return String.valueOf(level);
            case "score":
                int score = MySQL.getScore(player.getUniqueId().toString());
                return String.valueOf(score);
            case "required_score":
                int levelnum = MySQL.getLevel(player.getUniqueId().toString());
                int requiredScore = LevelSystem.getExperienceForLevel(levelnum + 1);
                int currentScore = MySQL.getScore(player.getUniqueId().toString());
                float percentage = requiredScore != 0 ? (float) currentScore / requiredScore * 100.0f : 0;
                percentage = Math.max(1, Math.min(100, percentage)); // Ensure the percentage is between 1 and 100
                return String.format("%.1f%%", percentage);
            case "killstreak":
                return String.valueOf(MySQL.getKillStreak(player.getUniqueId().toString()));
            default:
                return null;
        }
    }

    private String createProgressBar(float percentage) {
        int totalBars = 10;
        int filledBars = (int) (totalBars * percentage / 100.0f);
        return "§b" + repeat("■", filledBars) + "§7" + repeat("■", totalBars - filledBars);
    }

    private String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
}
