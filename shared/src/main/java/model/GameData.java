package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername,
                       String gameName, ChessGame game) {
    public String getWhiteColor(){
        return whiteUsername;
    }
    public String getBlackColor(){
        return blackUsername;
    }
    public int getGameID(){return gameID;}
    public String getGameName(){
        return gameName;
    }

}
