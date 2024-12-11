package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;

public class LoadGame extends ServerMessage{
    //private ChessBoard game;
    ChessGame game;
    private boolean gameOver;
    String whiteOrBlack = "white";
    //private boolean whiteOrblack = true;
    public LoadGame(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.gameOver = game.getIsItGameOver();
    }

    public void setColorToWhiteBlack(String whiteorb){
        if(whiteorb.equalsIgnoreCase("white")){
            this.whiteOrBlack = "white";
        }
        else{
            this.whiteOrBlack = "black";
        }
    }

    public String getWhiteOrBlack() {
        return whiteOrBlack;
    }

    public ChessGame getGame() {
        return game;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
