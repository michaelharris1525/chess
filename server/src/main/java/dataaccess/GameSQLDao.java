package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UnabletoConfigureDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameSQLDao implements GameDataAccess {
    private final Gson gson = new Gson();
    private boolean gameOver = false;
    // Method to add a new game
    @Override
    public void addNewGame(int id, String gameName, ChessGame game) {
        String sql = "INSERT INTO gameData (gameID, gameName, gameState) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, gameName);
            stmt.setString(3, gson.toJson(game)); // Serialize ChessGame to JSON
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void setGameOverInJson(int gameId) {
        String sql = "UPDATE gameData SET gameState = JSON_SET(gameState, '$.gameOver', true) WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameId);
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update gameOver in JSON", e);
        }
    }

//    @Override
//    public void updateBoard(int gameId, ChessGame game) {
//
//    }

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
        String query = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM gameData WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, key);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int gameId = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String gameState = rs.getString("gameState");

                // Deserialize the ChessGame from JSON
                ChessGame chessGame = gson.fromJson(gameState, ChessGame.class);

                return new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame);
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    public boolean gameExists(int gameId) {
        String query = "SELECT 1 FROM gameData WHERE gameID = ? LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update the game state
    public void updateGameState(int gameId, ChessGame game) {
        String sql = "UPDATE gameData SET gameState = ? WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gson.toJson(game)); // Serialize ChessGame to JSON
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
        String query = "SELECT gameID, whiteUsername, blackUsername, gameName, gameState FROM gameData";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int gameId = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String gameState = rs.getString("gameState");

                // Deserialize the ChessGame from JSON
                ChessGame chessGame = gson.fromJson(gameState, ChessGame.class);

                games.add(new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame));
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return games;
    }

//    // Check if the table is empty
//    @Override
//    public boolean isEmpty() {
//        String query = "SELECT COUNT(*) FROM gameData";
//        try (Connection conn = DatabaseManager.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return rs.getInt(1) == 0;
//            }
//        } catch (SQLException | DataAccessException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

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

    private final String[] createTables = {
            """
        CREATE TABLE IF NOT EXISTS gameData (
            gameID INT PRIMARY KEY,
            whiteUsername VARCHAR(255),
            blackUsername VARCHAR(255),
            gameName VARCHAR(255),
            gameState TEXT
        );
        """
    };

    public void gameconfigureDatabase() throws UnabletoConfigureDatabase, DataAccessException {
        DatabaseManager.configureDatabase(createTables);
    }
}
