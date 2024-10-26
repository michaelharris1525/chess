package dataaccess;

import model.AuthData;
import model.GameData;

public interface GameDataAccess {
    int getSize();
    void addNewGame(int id);
    void clearGameData();
    boolean isGameReal(int gameId);
    GameData getGameData(int key);
    void UpdateWhiteColor(int gameId, String white);
    void UpdateBlackColor(int gameId, String black);
}
