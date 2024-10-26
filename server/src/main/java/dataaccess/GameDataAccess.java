package dataaccess;

import model.AuthData;

public interface GameDataAccess {
    int getSize();
    void addNewGame(int id);
    void clearGameData();
    boolean isGameReal(int gameId);
}
