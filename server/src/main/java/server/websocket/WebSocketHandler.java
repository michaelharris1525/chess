package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthSQLTokenClass;
import dataaccess.GameSQLDao;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notifications;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameSQLDao gameDao = new GameSQLDao();
    private AuthSQLTokenClass authDao = new AuthSQLTokenClass();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, InvalidMoveException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        if(authDao.getauthtoken(action.getAuthToken()) == null){
            sendErrorMessage(session, "Unauthorized auth token");
            return;
        }

        // updating the game username when leaving
        if(action.getCommandType() == UserGameCommand.CommandType.LEAVE){
            GameData gameDatatoLeave = gameDao.getGameData(action.getGameID());

            AuthData authD = authDao.getauthtoken(action.getAuthToken());
            String username = authD.username();
            if(username == null){
                sendErrorMessage(session, "User not found");
                return;
            }
            if(Objects.equals(gameDatatoLeave.whiteUsername(), username)){
                gameDao.updateWhiteColor(action.getGameID(), null);
            }
            else if(Objects.equals(gameDatatoLeave.blackUsername(), username)){
                gameDao.updateBlackColor(action.getGameID(), null);
            }
        }
        AuthData authDadfaf = authDao.getauthtoken(action.getAuthToken());
        switch (action.getCommandType()) {
            case CONNECT -> {
                if(enterGameAttempt(action.getGameID(), action.getAuthToken())){
                    enterGameSuccesful(authDadfaf.username(), session, action.getGameID(),action.getWhiteblack());
                }
                else {
                    sendErrorMessage(session, "gameId or your input is wrong");
                }
            }
            case LEAVE -> {exit(authDadfaf.username(), action);}
            case MAKE_MOVE -> {makeMove(action, session); }
            case RESIGN -> {resign(authDadfaf.username(), session, action);}
            case VALID -> {validMoves(action, session);}
            case DISPLAY -> {displayboard(authDadfaf.username(), session, action.getGameID(),action.getWhiteblack()
            ,action);}
        }
    }
    private void displayboard(String username, Session session, int gameId, String whiteBlack, UserGameCommand action) throws IOException {
        ChessGame gamegame = gameDao.getGameData(gameId).game();
        LoadGame gameM = new LoadGame(gamegame, null, whiteBlack);
        String game = new Gson().toJson(gameM);
        session.getRemote().sendString(game);
    }
    private void exit(String visitorName, UserGameCommand command) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notifications(message);
        connections.broadcast(visitorName, notification, command.getGameID());

    }
    private void resign(String authToken, Session session, UserGameCommand action) throws IOException {
        //grabbing the game data by Game ID
        GameData gameDatatoLeave = gameDao.getGameData(action.getGameID());
        //grabbing User Info
        AuthData authD = authDao.getauthtoken(action.getAuthToken());
        if(!gameDatatoLeave.game().getIsItGameOver()) {
            if (authD.username().equals(gameDatatoLeave.blackUsername()) ||
                    authD.username().equals(gameDatatoLeave.whiteUsername())) {
                gameDatatoLeave.game().setGameOver();
                gameDao.setGameOverInJson(action.getGameID());
                var message = String.format("%s has resigned the game", authToken);
                var notification = new Notifications(message);
                connections.broadcast(null, notification, action.getGameID());
            } else {
                sendErrorMessage(session, "You are not playing game Observer");
            }
        }
        else {
            sendErrorMessage(session, "GAME IS ALREADY OVER JACK");
        }
    }
    private void sendErrorMessage(Session session, String errorMessage) throws IOException {
        ErrorMessage m  = new ErrorMessage("Error: " + errorMessage);
        String error = new Gson().toJson(m);
        session.getRemote().sendString(error);
    }

    private boolean enterGameAttempt(int gameId, String authToken){
        //check game Id is correct
            if(gameDao.gameExists(gameId)){
                if(authDao.containsAuthToken(authToken)){
                    return true;
                }
            }
            return false;
    }
    private void enterGameSuccesful(String visitorName, Session session, int gameId, String whiteBlack) throws IOException {
        connections.add(visitorName, session, gameId);
        String message = String.format("%s is in the game for now", visitorName);
        var notification = new Notifications(message);
        connections.broadcast(visitorName, notification, gameId);
        ChessGame gamegame = gameDao.getGameData(gameId).game();
        LoadGame gameM = new LoadGame(gamegame, null, whiteBlack);

        String game = new Gson().toJson(gameM);
        if (whiteBlack != null) {
            gameM.setColorToWhiteBlack(whiteBlack);
            if (whiteBlack.equalsIgnoreCase("White")) {
                gameDao.updateWhiteColor(gameId, visitorName);
            } else if(whiteBlack.equalsIgnoreCase("Black")) {
                gameDao.updateBlackColor(gameId, visitorName);
            }
        }
        session.getRemote().sendString(game);
    }
    private void validMoves(UserGameCommand action,Session session) throws IOException {
        GameData gameGame = gameDao.getGameData(action.getGameID());
        Collection<ChessMove> collectionOfMoves = gameGame.game().validMoves(action.getPosition());
        ChessGame gamegame = gameDao.getGameData(action.getGameID()).game();
        LoadGame updateBoardd = new LoadGame(gamegame, collectionOfMoves, action.getWhiteblack());
        connections.broadcast(null, updateBoardd, action.getGameID());

    }

    private void makeMove(UserGameCommand action, Session session) throws IOException, InvalidMoveException {
        // Extract the realMove from the command
        try {
            String whiteorBlackJustForOneCase = null;
            GameData gameData = gameDao.getGameData(action.getGameID());
            if (gameData == null) {
                System.out.println("Game not found.");
                return;
            }
            if(gameData.game().getIsItGameOver() == true){
                sendErrorMessage(session, "Game is Over");
                return;
            }
            ChessMove move = action.getRealMove();
            ChessBoard board = gameData.game().getBoard();
            if (board == null) {
                System.out.println("Game not found for the given authToken.");
                return;
            }
            //check if user color matches piece color
            AuthData authData = authDao.getauthtoken(action.getAuthToken());
            ChessPiece piece = board.getPiece(move.getStartPosition());
            if(!gameData.game().getIsItGameOver()) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (gameData.getWhiteColor().equals(authData.username())) {
                        gameData.game().makeMove(move);
                        board = gameData.game().getBoard();
                        gameData.game().setBoard(board);
                        if(action.getWhiteblack() == null){
                            whiteorBlackJustForOneCase = "white";
                        }
                    } else {
                        sendErrorMessage(session, "Invalid Move!");
                        return;
                    }
                } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (gameData.getBlackColor().equals(authData.username())) {
                        // Attempt to make the move on the board
                        gameData.game().makeMove(move);
                        board = gameData.game().getBoard();
                        gameData.game().setBoard(board);
                        if(action.getWhiteblack() == null){
                            whiteorBlackJustForOneCase = "black";
                        }
                    } else {
                        sendErrorMessage(session, "Invalid Move!");
                        return;
                    }
                } else {
                    sendErrorMessage(session, "Wrong color username");
                    return;
                }
            }
            //game over then print out.


            //update board into database
            gameDao.updateGameState(action.getGameID(),gameData.game());
            // Broadcast the updated board state to all players
            ChessGame gamegame = gameDao.getGameData(action.getGameID()).game();


            LoadGame updateBoardd = new LoadGame(gamegame, null, whiteorBlackJustForOneCase);
            //String toUserBoard = new Gson().toJson(updateBoardd);

            Notifications update = new Notifications(" moved ");
            //check if the game is really over if it is, send different message
            if(gameData.game().getIsItGameOver()) {
                endGameSendMessage(updateBoardd, action);
            }
            else {
                //send the message of it being updated to the users
                AuthData authdata3 = authDao.getauthtoken(action.getAuthToken());
                connections.broadcast(authdata3.username(), update, action.getGameID());
                connections.broadcast(null, updateBoardd, action.getGameID());

            }
        }
        catch (InvalidMoveException e) {
            // Handle invalid move
            sendErrorMessage(session, "invalid move can't do that JACK!");
        }
    }
    private void endGameSendMessage(LoadGame board, UserGameCommand action) throws IOException {
        //send the message of it being updated to the users
        AuthData authdata3 = authDao.getauthtoken(action.getAuthToken());
        Notifications gameOverSon = new Notifications(
                "GAME OVER!!! " + authdata3.username() + " WINS");
        connections.broadcast(authdata3.username(), gameOverSon, action.getGameID());
        connections.broadcast(null, board, action.getGameID());
    }

}