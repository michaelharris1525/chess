package websocket.messages;

import chess.ChessBoard;

public class LoadGame extends ServerMessage{
    private ChessBoard game;
    public LoadGame(ChessBoard game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessBoard getGame() {
        return game;
    }

}
