package model;

public class JoinGameRequestReal {
    private String playerColor;
    private Integer gameID;

    public JoinGameRequestReal(int gameId, String whiteBlack) {
        this.gameID = gameId;
        this.playerColor = whiteBlack;
    }
    public int getRealGameId(){
        return gameID;
    }
    public String getTeamColor(){
        return playerColor;
    }
}
