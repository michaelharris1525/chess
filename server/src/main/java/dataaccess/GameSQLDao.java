package dataaccess;

import model.GameData;
import model.UnabletoConfigureDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameSQLDao implements GameDataAccess{

    // Method to add a new game
    @Override
    public void addNewGame(int id, String gameName) {
        String sql = "INSERT INTO gameData (gameID, gameName) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, gameName);
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    // Method to clear all game data
    @Override
    public void clearGameData() {
        String sql = "DELETE FROM gameData";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a game by ID
    @Override
    public GameData getGameData(int key) {
        String query = "SELECT gameID, whiteUsername, blackUsername, gameName FROM gameData WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, key);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int gameId = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                return new GameData(gameId, whiteUsername, blackUsername, gameName, null); // Adjust as needed for `ChessGame`
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to update the white player username for a game
    @Override
    public void updateWhiteColor(int gameId, String white) {
        String sql = "UPDATE gameData SET whiteUsername = ? WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, white);
            stmt.setInt(2, gameId);
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    // Method to update the black player username for a game
    @Override
    public void updateBlackColor(int gameId, String black) {
        String sql = "UPDATE gameData SET blackUsername = ? WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, black);
            stmt.setInt(2, gameId);
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    // Method to get all games
    @Override
    public Collection<GameData> getAllGameData() {
        List<GameData> games = new ArrayList<>();
        String query = "SELECT gameID, whiteUsername, blackUsername, gameName FROM gameData";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int gameId = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                games.add(new GameData(gameId, whiteUsername, blackUsername, gameName, null)); // Adjust as needed for `ChessGame`
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return games;
    }

    // Method to check if the game data table is empty
    @Override
    public boolean isEmpty() {
        String query = "SELECT COUNT(*) FROM gameData";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public int getSize() {
        String query = "SELECT COUNT(*) FROM gameData";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }
//    @Override
//    public int getSize() {
//        return 0;
//    }
//
//    @Override
//    public void addNewGame(int id, String gameName) {
//
//    }
//
//    @Override
//    public void clearGameData() {
//
//    }
//
//    @Override
//    public GameData getGameData(int key) {
//        return null;
//    }
//
//    @Override
//    public void updateWhiteColor(int gameId, String white) {
//
//    }
//
//    @Override
//    public void updateBlackColor(int gameId, String black) {
//
//    }
//
//    @Override
//    public Collection<GameData> getAllGameData() {
//        return List.of();
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
    //int gameID, String whiteUsername, String blackUsername,
    // String gameName, ChessGame game
    private final String[] createTables =  {
            """
            CREATE TABLE IF NOT EXISTS gameData (
                `gameID` int PRIMARY KEY,
                `whiteUsername` VARCHAR(255),
                `blackUsername` VARCHAR(255),
                `gameName` VARCHAR(255)
            );
            """};



    public void configureDatabase() throws UnabletoConfigureDatabase, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createTables) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new UnabletoConfigureDatabase(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}

/*
  String sql = "INSERT INTO games (gameID, gameName) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, gameName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error adding game to SQL database: " + e.getMessage(), e);
        }
 */