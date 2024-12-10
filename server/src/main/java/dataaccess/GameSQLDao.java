package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
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

    @Override
    public void updateBoard(int keyId){
        GameData game = getGameData(keyId);
    }

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
        String query = "SELECT gameID, whiteUsername, blackUsername, gameName FROM gameData, chessBoard WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, key);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int gameId = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String chessboard = rs.getString("chessBoard");

                ChessGame game = new  Gson().fromJson(chessboard, ChessGame.class);

                return new GameData(gameId, whiteUsername, blackUsername, gameName, game); // Adjust as needed for `ChessGame`
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean gameExists(int gameId) {
        String query = "SELECT 1 FROM gameData WHERE gameID = ? LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if the game exists, false otherwise
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return false; // In case of error, assume game doesn't exist
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
        String query = "SELECT gameID, whiteUsername, blackUsername, gameName, chessBoard FROM gameData";
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
    private final String[] createTables =  {
            """
            CREATE TABLE IF NOT EXISTS gameData (
                `gameID` int PRIMARY KEY,
                `whiteUsername` VARCHAR(255),
                `blackUsername` VARCHAR(255),
                `gameName` VARCHAR(255),
                'chessBoard' LONG TEXT
            );
            """};

    public void gameconfigureDatabase() throws UnabletoConfigureDatabase, DataAccessException {
        DatabaseManager.configureDatabase(createTables);
    }
}