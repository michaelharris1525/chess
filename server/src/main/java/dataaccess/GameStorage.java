package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public class GameStorage implements GameDataAccess{
    //string authtoken and string username
    final private HashMap<Integer, GameData> gameDatas = new HashMap<>();

    public Collection<GameData> getAllGameData(){
        return gameDatas.values();
    }

    public int getSize(){
        return gameDatas.size();
    }
    public void updateWhiteColor(int gameId, String newusername){
        GameData existingGame = gameDatas.get(gameId);
        if(existingGame != null){
            gameDatas.put(existingGame.gameID(),
                    new GameData(existingGame.gameID(), newusername, existingGame.blackUsername(),
                            existingGame.gameName(), existingGame.game()));
        }
    }
    public void updateBlackColor(int gameId, String newusername){
        GameData existingGame = gameDatas.get(gameId);
        if(existingGame != null){
            gameDatas.put(existingGame.gameID(),
                    new GameData(existingGame.gameID(), existingGame.whiteUsername(), newusername,
                            existingGame.gameName(), existingGame.game()));
        }
    }
    public GameData getGameData(int key){
        return gameDatas.get(key);
    }
    public void addNewGame(int id, String gameName){
        gameDatas.put(id, new GameData(id, null, null, gameName, new ChessGame()));
    }
    public void clearGameData(){
        gameDatas.clear();
    }

    public boolean isEmpty() {
        if(gameDatas.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public boolean gameExists(int gameId) {
        return gameDatas.containsKey(gameId);
    }

//    public boolean gameExists(int gameId) {
//        String query = "SELECT 1 FROM gameData WHERE gameID = ? LIMIT 1";
//        try (Connection conn = DatabaseManager.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, gameId);
//            ResultSet rs = stmt.executeQuery();
//            return rs.next(); // Returns true if the game exists, false otherwise
//        } catch (SQLException | DataAccessException e) {
//            e.printStackTrace();
//        }
//        return false; // In case of error, assume game doesn't exist
//    }


}