package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public interface GameDataAccess {
    int getSize();
    void addNewGame(int id, String GameName);
    void clearGameData();
    //boolean isGameReal(int gameId);
    GameData getGameData(int key);
    void UpdateWhiteColor(int gameId, String white);
    void UpdateBlackColor(int gameId, String black);
    Collection<GameData> getAllGameData();
    Collection<Integer> getAllKeysInts();
}