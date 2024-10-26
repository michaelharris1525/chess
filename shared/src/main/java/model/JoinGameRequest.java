package model;

public class JoinGameRequest {
    private String playerColor;
    private Integer gameID;

    public String getPlayerColor() {
        return playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }
    public void updatePlayerWhite(){
        playerColor = "WHITE";
    }
    public void updatePlayerBlack(){
        playerColor = "BLACK";
    }
}

