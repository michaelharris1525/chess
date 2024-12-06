package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public interface GameDataAccess {
    int getSize();
    void addNewGame(int id, String gameName);
    void clearGameData();
    GameData getGameData(int key);
    void updateWhiteColor(int gameId, String white);
    void updateBlackColor(int gameId, String black);
    Collection<GameData> getAllGameData();
    boolean isEmpty();
    public boolean gameExists(int gameId);
}