package net.reefrealm.reefrealmskypvp.data;

import java.sql.*;

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
                "  PRIMARY KEY (`id`));";

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            // executing the queries
            stmt.executeUpdate(query1);
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

}