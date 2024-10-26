package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class GameStorage implements GameDataAccess{
    //string authtoken and string username
    final private HashMap<Integer, GameData> gameDatas = new HashMap<>();

    public int getSize(){
        return gameDatas.size();
    }
    public void UpdateWhiteColor(int gameId, String white){
        gameDatas.put(gameId, new GameData(gameId, white, null, null, new ChessGame()));
    }
    public void UpdateBlackColor(int gameId, String black){
        gameDatas.put(gameId, new GameData(gameId, null, black, null, new ChessGame()));
    }
    public GameData getGameData(int key){
        return gameDatas.get(key);
    }
    public void addNewGame(int id){
        gameDatas.put(id, new GameData(id, null, null, null, new ChessGame()));
    }
    public void clearGameData(){
        gameDatas.clear();
    }

    public boolean isGameReal(int gameId){
        return gameDatas.containsKey(gameId);
    }

}
