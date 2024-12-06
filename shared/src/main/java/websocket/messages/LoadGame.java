package websocket.messages;

import chess.ChessBoard;

public class LoadGame extends ServerMessage{
    private ChessBoard game;
    public LoadGame(ServerMessageType type, String message, ChessBoard game) {
        super(type, message);
        this.game = game;
    }

    public ChessBoard getGame() {
        return game;
    }

}
