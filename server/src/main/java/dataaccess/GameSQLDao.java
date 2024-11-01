package dataaccess;

import model.GameData;
import model.UnabletoConfigureDatabase;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class GameSQLDao implements GameDataAccess{
    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void addNewGame(int id, String gameName) {

    }

    @Override
    public void clearGameData() {

    }

    @Override
    public GameData getGameData(int key) {
        return null;
    }

    @Override
    public void updateWhiteColor(int gameId, String white) {

    }

    @Override
    public void updateBlackColor(int gameId, String black) {

    }

    @Override
    public Collection<GameData> getAllGameData() {
        return List.of();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
    //int gameID, String whiteUsername, String blackUsername,
    // String gameName, ChessGame game
    private final String[] createTables =  {
            """
            CREATE TABLE IF NOT EXISTS gameData (
                'gameID' int PRIMARY KEY,
                'whiteUsername' VARCHAR(255),
                'blackUsername' VARCHAR(255),
                'gameName' VARCHAR(255)
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