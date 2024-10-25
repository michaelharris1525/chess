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
    public void addNewGame(int id){
        gameDatas.put(id, new GameData(id, null, null, null, new ChessGame()));
    }

}
