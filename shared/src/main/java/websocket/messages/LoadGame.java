package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;

public class LoadGame extends ServerMessage{
    //private ChessBoard game;
    ChessGame game;
    private boolean gameOver;
    public LoadGame(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.gameOver = game.getIsItGameOver();
    }

    public ChessGame getGame() {
        return game;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
