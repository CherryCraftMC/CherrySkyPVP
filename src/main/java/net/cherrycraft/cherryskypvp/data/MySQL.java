package net.cherrycraft.cherryskypvp.data;

import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

public class MySQL {

    static Connection connection;
    private static String host;
    private static String database;
    private static String user;
    private static String password;
    private static int port;

    public static void Login(String host, String database, String user, String password, int port) {
        MySQL.host = host;
        MySQL.database = database;
        MySQL.user = user;
        MySQL.password = password;
        MySQL.port = port;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection GetConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void createTables() {
        Connection connection = GetConnection();

        String query1 = "CREATE TABLE IF NOT EXISTS `ReefRealm_SkyPVP_Stats` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `uuid` VARCHAR(36) NOT NULL,\n" +
                "  `kills` INT DEFAULT 0,\n"+
                " `deaths` INT DEFAULT 0,\n"+
                "`KD` FLOAT DEFAULT '0.0',\n"+
                "`killstreak` INT DEFAULT 0,\n"+
                "`score` INT DEFAULT 0,\n"+
                "`level` INT DEFAULT 0,\n"+
                "`coins` INT DEFAULT 0,\n"+
                "  PRIMARY KEY (`id`));";

        String query2 = "CREATE TABLE IF NOT EXISTS `ReefRealm_SkyPVP_Bountys` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `uuid` VARCHAR(36) NOT NULL,\n" +
                "  `bounty` INT DEFAULT 0,\n"+
                "  PRIMARY KEY (`id`));";

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            // executing the queries
            stmt.executeUpdate(query1);
            stmt.executeUpdate(query2);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean userExists(String playerUUID) {
        Connection connection = GetConnection();
        String query = "SELECT * FROM ReefRealm_SkyPVP_Stats WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createUser(String playerUUID) {
        Connection connection = GetConnection();
        String query = "INSERT INTO ReefRealm_SkyPVP_Stats (uuid, kills, deaths, KD) VALUES (?, 0, 0, 0.0)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void Disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void incrementKills(String playerUUID) {
        if (!userExists(playerUUID)) {
            createUser(playerUUID);
        }
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Stats SET kills = kills + 1 WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void incrementDeaths(String playerUUID) {
        if (!userExists(playerUUID)) {
            createUser(playerUUID);
        }
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Stats SET deaths = deaths + 1 WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateKD(String playerUUID, float kd) {
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Stats SET KD = ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setFloat(1, kd);
            statement.setString(2, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void calculateKD(String playerUUID) {
        if (!userExists(playerUUID)) {
            createUser(playerUUID);
        }
        Connection connection = GetConnection();
        String query = "SELECT kills, deaths FROM ReefRealm_SkyPVP_Stats WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int kills = resultSet.getInt("kills");
                int deaths = resultSet.getInt("deaths");
                float kd = deaths != 0 ? (float) kills / deaths : kills;
                updateKD(playerUUID, kd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void incrementKillStreak(String playerUUID) {
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Stats SET killstreak = killstreak + 1 WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetKillStreak(String playerUUID) {
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Stats SET killstreak = 0 WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getKills(String playerUUID) {
        Connection connection = GetConnection();
        String query = "SELECT kills FROM ReefRealm_SkyPVP_Stats WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("kills");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDeaths(String playerUUID) {
        Connection connection = GetConnection();
        String query = "SELECT deaths FROM ReefRealm_SkyPVP_Stats WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("deaths");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float getKD(String playerUUID) {
        Connection connection = GetConnection();
        String query = "SELECT KD FROM ReefRealm_SkyPVP_Stats WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getFloat("KD");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getKillStreak(String playerUUID) {
        Connection connection = GetConnection();
        String query = "SELECT killstreak FROM ReefRealm_SkyPVP_Stats WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("killstreak");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getScore(String playerUUID) {
        Connection connection = GetConnection();
        String query = "SELECT score FROM ReefRealm_SkyPVP_Stats WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setScore(String playerUUID, int score) {
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Stats SET score = ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, score);
            statement.setString(2, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getLevel(String playerUUID) {
        Connection connection = GetConnection();
        String query = "SELECT level FROM ReefRealm_SkyPVP_Stats WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setLevel(String playerUUID, int level) {
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Stats SET level = ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, level);
            statement.setString(2, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCoins(String playerUUID, int amount){
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Stats SET coins = coins + ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, amount);
            statement.setString(2, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void overrideCoinsAmount(String playerUUID, int amount){
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Stats SET coins = ? WHERE uuid = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, amount);
            statement.setString(2, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static int getCoins(String playerUUID) {
        Connection connection = GetConnection();
        String query = "SELECT coins FROM ReefRealm_SkyPVP_Stats WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void removeCoins(String playerUUID, int amount) {
        int currentCoins = getCoins(playerUUID);
        if (currentCoins >= amount) {
            overrideCoinsAmount(playerUUID, currentCoins - amount);
        } else {
            overrideCoinsAmount(playerUUID, 0);
        }
    }

    public static int getBounty(String playerUUID) {
        Connection connection = GetConnection();
        String query = "SELECT bounty FROM ReefRealm_SkyPVP_Bountys WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("bounty");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setBounty(String playerUUID, int amount) {
        int currentBounty = getBounty(playerUUID);
        Connection connection = GetConnection();
        String query = "UPDATE ReefRealm_SkyPVP_Bountys SET bounty = ? WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, currentBounty + amount);
            statement.setString(2, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean checkifBountyExist(String playerUUID){
        Connection connection = GetConnection();
        String query = "SELECT * FROM ReefRealm_SkyPVP_Bountys WHERE uuid = ?";
        if (!userExists(playerUUID)) {
            return true;
        }
        return false;
    }

    public static void createBounty(String playerUUID, int amount) {
        Connection connection = GetConnection();
        if (getBounty(playerUUID) == 0) {
            String query = "INSERT INTO ReefRealm_SkyPVP_Bountys (uuid, bounty) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerUUID);
                statement.setInt(2, amount);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            setBounty(playerUUID, amount);
        }
    }

    public static void removeBountyfromExistance(String playerUUID) {
        Connection connection = GetConnection();
        String query = "DELETE FROM ReefRealm_SkyPVP_Bountys WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUUID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int leaderBoardKillStats(int topnumber){
        Connection connection = GetConnection();
        String query = "SELECT * FROM ReefRealm_SkyPVP_Stats ORDER BY kills DESC LIMIT ?, 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, topnumber - 1);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int kills = resultSet.getInt("kills");
                return kills;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String leaderBoardPlayerKillStats(int topnumber){
        Connection connection = GetConnection();
        String query = "SELECT * FROM ReefRealm_SkyPVP_Stats ORDER BY kills DESC LIMIT ?, 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, topnumber - 1);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String playerUUID = resultSet.getString("uuid");
                String playerName = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).getName();
                return playerName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}