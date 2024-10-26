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
    public Collection<Integer> getAllKeysInts(){
        return gameDatas.keySet();
    }
    public int getSize(){
        return gameDatas.size();
    }
    public void UpdateWhiteColor(int gameId, String newusername){
        GameData existingGame = gameDatas.get(gameId);
        if(existingGame != null){
            gameDatas.put(existingGame.gameID(),
                    new GameData(existingGame.gameID(), newusername, existingGame.blackUsername(),
                            existingGame.gameName(), existingGame.game()));
        }
        //gameDatas.put(gameId, new GameData(gameId, username, null, null, new ChessGame()));
    }
    public void UpdateBlackColor(int gameId, String newusername){
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
    public void addNewGame(int id, String GameName){
        gameDatas.put(id, new GameData(id, null, null, GameName, new ChessGame()));
    }
    public void clearGameData(){
        gameDatas.clear();
    }

//    public boolean isGameReal(int gameId){
//        return gameDatas.containsKey(gameId);
//    }

}