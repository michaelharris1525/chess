package model;

import java.util.HashMap;
import java.util.List;

public class ListGames {
    private final int gameID;
    private HashMap<Integer, GameData> ListOfGames;

    public ListGames(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public void updateListGames(HashMap<Integer, GameData> gameDatafromAcess){
        ListOfGames = gameDatafromAcess;
    }
}

