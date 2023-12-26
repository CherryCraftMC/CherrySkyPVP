package net.cherrycraft.cherryskypvp.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.cherrycraft.cherryskypvp.data.MySQL;
import net.cherrycraft.cherryskypvp.level.LevelSystem;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
            case "leaderboard_top_kills_kills_1":
                return String.valueOf(MySQL.leaderBoardKillStats(1));
            case "leaderboard_top_kills_kills_2":
                return String.valueOf(MySQL.leaderBoardKillStats(2));
            case "leaderboard_top_kills_kills_3":
                return String.valueOf(MySQL.leaderBoardKillStats(3));
            case "leaderboard_top_kills_kills_4":
                return String.valueOf(MySQL.leaderBoardKillStats(4));
            case "leaderboard_top_kills_kills_5":
                return String.valueOf(MySQL.leaderBoardKillStats(5));
            case "leaderboard_top_kills_kills_6":
                return String.valueOf(MySQL.leaderBoardKillStats(6));
            case "leaderboard_top_kills_kills_7":
                return String.valueOf(MySQL.leaderBoardKillStats(7));
            case "leaderboard_top_kills_kills_8":
                return String.valueOf(MySQL.leaderBoardKillStats(8));
            case "leaderboard_top_kills_kills_9":
                return String.valueOf(MySQL.leaderBoardKillStats(9));
            case "leaderboard_top_kills_kills_10":
                return String.valueOf(MySQL.leaderBoardKillStats(10));
            case "leaderboard_top_kills_kills_11":
                return String.valueOf(MySQL.leaderBoardKillStats(11));
            case "leaderboard_top_kills_kills_12":
                return String.valueOf(MySQL.leaderBoardKillStats(12));
            case "leaderboard_top_kills_kills_13":
                return String.valueOf(MySQL.leaderBoardKillStats(13));
            case "leaderboard_top_kills_kills_14":
                return String.valueOf(MySQL.leaderBoardKillStats(14));
            case "leaderboard_top_kills_kills_15":
                return String.valueOf(MySQL.leaderBoardKillStats(15));

            case "leaderboard_top_kills_player_1":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(1));
            case "leaderboard_top_kills_player_2":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(2));
            case "leaderboard_top_kills_player_3":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(3));
            case "leaderboard_top_kills_player_4":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(4));
            case "leaderboard_top_kills_player_5":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(5));
            case "leaderboard_top_kills_player_6":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(6));
            case "leaderboard_top_kills_player_7":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(7));
            case "leaderboard_top_kills_player_8":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(8));
            case "leaderboard_top_kills_player_9":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(9));
            case "leaderboard_top_kills_player_10":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(10));
            case "leaderboard_top_kills_player_11":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(11));
            case "leaderboard_top_kills_player_12":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(12));
            case "leaderboard_top_kills_player_13":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(13));
            case "leaderboard_top_kills_player_14":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(14));
            case "leaderboard_top_kills_player_15":
                return String.valueOf(MySQL.leaderBoardPlayerKillStats(15));
            case "required_score":
                int levelnum = MySQL.getLevel(player.getUniqueId().toString());
                int requiredScore = LevelSystem.getExperienceForLevel(levelnum + 1);
                int currentScore = MySQL.getScore(player.getUniqueId().toString());
                float percentage = requiredScore != 0 ? (float) currentScore / requiredScore * 100.0f : 0;
                percentage = Math.max(1, Math.min(100, percentage)); // Ensure the percentage is between 1 and 100
                return String.format("%.1f%%", percentage);
            case "killstreak":
                return String.valueOf(MySQL.getKillStreak(player.getUniqueId().toString()));
            case "coins":
                return String.valueOf(MySQL.getCoins(player.getUniqueId().toString()));
            default:
                return null;
        }
    }

    MiniMessage miniMessage = MiniMessage.builder().build();
    LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

    private String createProgressBar(float percentage) {
        int totalBars = 10;
        int filledBars = (int) (totalBars * percentage / 100.0f);
        return serializer.serialize(miniMessage.deserialize("<aqua>")) + repeat("■", filledBars) + serializer.serialize(miniMessage.deserialize("<gray>")) + repeat("■", totalBars - filledBars);
    }

    private String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
}
