package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameStorage implements GameDataAccess {
    private final HashMap<Integer, GameData> gameDatas = new HashMap<>();

    @Override
    public void addNewGame(int id, String gameName, ChessGame game) {
        gameDatas.put(id, new GameData(id, null, null, gameName, game));
    }

    @Override
    public void updateWhiteColor(int gameId, String newUsername) {
        GameData existingGame = gameDatas.get(gameId);
        if (existingGame != null) {
            gameDatas.put(existingGame.gameID(),
                    new GameData(existingGame.gameID(), newUsername, existingGame.blackUsername(),
                            existingGame.gameName(), existingGame.game()));
        }
    }

    @Override
    public void updateBlackColor(int gameId, String newUsername) {
        GameData existingGame = gameDatas.get(gameId);
        if (existingGame != null) {
            gameDatas.put(existingGame.gameID(),
                    new GameData(existingGame.gameID(), existingGame.whiteUsername(), newUsername,
                            existingGame.gameName(), existingGame.game()));
        }
    }

    @Override
    public GameData getGameData(int key) {
        return gameDatas.get(key);
    }

    @Override
    public boolean gameExists(int gameId) {
        return gameDatas.containsKey(gameId);
    }

    @Override
    public Collection<GameData> getAllGameData() {
        return gameDatas.values();
    }

    @Override
    public void clearGameData() {
        gameDatas.clear();
    }

    @Override
    public int getSize() {
        return gameDatas.size();
    }
}
