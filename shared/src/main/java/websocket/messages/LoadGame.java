package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class LoadGame extends ServerMessage{
    //private ChessBoard game;
    ChessGame game;
    private boolean gameOver;
    String whiteOrBlack;
    Collection<ChessMove> moves;
    //private boolean whiteOrblack = true;
    public LoadGame(ChessGame game, Collection<ChessMove> moves) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.gameOver = game.getIsItGameOver();
        this.moves = moves;
    }

    public Collection<ChessMove> getMoves() {
        return moves;
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
