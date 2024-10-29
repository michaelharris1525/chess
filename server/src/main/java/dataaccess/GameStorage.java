package dataaccess;

import chess.ChessGame;
import model.GameData;

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
        gameDatas.put(id, new GameData(id, null, null, GameName, new ChessGame()));
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


}