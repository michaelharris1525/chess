package server.websocket;

import chess.*;
import com.google.gson.Gson;
//import dataaccess.DataAccess;
import dataaccess.AuthSQLTokenClass;
import dataaccess.GameSQLDao;
import dataaccess.GameStorage;
import dataaccess.UserSQLDao;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
//import websocket.commands.MakeMoveCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notifications;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameSQLDao gameDao = new GameSQLDao();
    private AuthSQLTokenClass authDao = new AuthSQLTokenClass();
    private UserSQLDao userDao = new UserSQLDao();
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
                gameDao.updateWhiteColor(action.getGameID(), null);
            }
        }
//        else if(action.getCommandType() == UserGameCommand.CommandType.RESIGN){
//
//        }
        AuthData authDadfaf = authDao.getauthtoken(action.getAuthToken());
        switch (action.getCommandType()) {
            case CONNECT -> {
                if(enterGameAttempt(action.getGameID(), action.getAuthToken()) == true){
                    enterGameSuccesful(authDadfaf.username(), session, action.getGameID(),action.getWhiteblack());
                }
                else {
                    sendErrorMessage(session, "gameId or your input is wrong");
                }
            }

            case LEAVE -> {exit(authDadfaf.username(), action);}
            //case MAKE_MOVE -> makeMove(action, session);
            case MAKE_MOVE -> {
                makeMove(action, session);
            }
            case RESIGN -> {resign(authDadfaf.username(), session, action);}
        }
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
        String username = authD.username();

        if(!gameDatatoLeave.game().getIsItGameOver()) {
            if (authD.username().equals(gameDatatoLeave.blackUsername()) ||
                    authD.username().equals(gameDatatoLeave.whiteUsername())) {

                //set game to game over in gamedata, do not think it works for database
                gameDatatoLeave.game().setGameOver();

                gameDao.setGameOverInJson(action.getGameID());

                var message = String.format("%s has resigned the game", authToken);
                var notification = new Notifications(message);
                connections.broadcast(null, notification, action.getGameID());
                //ChessGame gamegame = gameDao.getGameData(action.getGameID()).game();
                // LoadGame gameboy = new LoadGame(gamegame);
                Notifications n = new Notifications("GAME OVER");

                String mes = new Gson().toJson(n);

                //session.getRemote().sendString(mes);
            } else {
                sendErrorMessage(session, "You are not playing game Observer");
                return;
            }
        }
        else {
            sendErrorMessage(session, "GAME IS ALREADY OVER JACK");
            return;
        }


    }

//    private void resign(String authToken, Session session) throws IOException {
//        connections.remove(authToken);
//        //userDao.get
//        var message = String.format("%s has resigned the game", authToken);
//        var notification = new Notifications(message);
//        connections.broadcast(authToken, notification);
//    }

    private void sendErrorMessage(Session session, String errorMessage) throws IOException {

        //ErrorMessage m  = new ErrorMessage("Error: You messed up the game key Id");
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

    //private void enterGameSuccesful(String visitorName, Session session, int gameId, String whiteBlack) throws IOException {
    private void enterGameSuccesful(String visitorName, Session session, int gameId, String whiteBlack) throws IOException {
        connections.add(visitorName, session, gameId);
        //userDao.get
        String message = String.format("%s is in the game auth token for now", authDao.getauthtoken(visitorName));
        var notification = new Notifications(message);
        connections.broadcast(visitorName, notification, gameId);
        //make a load game object, copy of board, change later to get
        //the actual board that is being played at its current state
        ChessGame gamegame = gameDao.getGameData(gameId).game();
        //ChessBoard board = gamegame.getBoard();
        LoadGame gameM = new LoadGame(gamegame);
        String game = new Gson().toJson(gameM);
        if (whiteBlack != null) {
            if (whiteBlack.equalsIgnoreCase("White")) {
                gameDao.updateWhiteColor(gameId, visitorName);
            } else if(whiteBlack.equalsIgnoreCase("Black")) {
                gameDao.updateBlackColor(gameId, whiteBlack);
            }
        }

//        Notifications message2 = new Notifications(message)
//
//        connections.broadcast(null, );
        //function to grab board
        session.getRemote().sendString(game);
        //connections.broadcast(visitorName, gameM);
    }


//    private void isGameOver(){
//
//    }



    private void makeMove(UserGameCommand action, Session session) throws IOException, InvalidMoveException {
        // Extract the realMove from the command
        try {

            GameData gameData = gameDao.getGameData(action.getGameID());
            if (gameData == null) {
                System.out.println("Game not found.");
                return;
            }
            if(gameData.game().getIsItGameOver() == true){
                sendErrorMessage(session, "FJSDFSDKFJS");
                return;
            }

            //this where the invalid move exception passes.
            ChessMove move = action.getRealMove();

            // Fetch the game board associated with the gameID or authToken
            //ChessBoard board = connections.getBoard(action.getAuthToken());
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
                    //problem is here, getting username when i should get the auth token
                    if (gameData.getWhiteColor().equals(authData.username())) {
                    //if (gameData.getWhiteColor().equals(authData.authToken())) {
                        // Attempt to make the move on the board
                        gameData.game().makeMove(move);
                        board = gameData.game().getBoard();
                        gameData.game().setBoard(board);
                    } else {
                        sendErrorMessage(session, "Wrong color username");
                        return;
                    }
                } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (gameData.getBlackColor().equals(authData.username())) {
                        // Attempt to make the move on the board
                        gameData.game().makeMove(move);
                        board = gameData.game().getBoard();
                        gameData.game().setBoard(board);
                    } else {
                        sendErrorMessage(session, "Wrong color username");
                        return;
                    }
                } else {
                    sendErrorMessage(session, "Wrong color username");
                    return;
                }
            }

            //update board into database
            gameDao.updateGameState(action.getGameID(),gameData.game());
            // Broadcast the updated board state to all players
            ChessGame gamegame = gameDao.getGameData(action.getGameID()).game();
            LoadGame updateBoardd = new LoadGame(gamegame);
            String toUserBoard = new Gson().toJson(updateBoardd);
            Notifications update = new Notifications(
                    "Move made");
            //send the message of it being updated to the users
            AuthData authdata3 = authDao.getauthtoken(action.getAuthToken());
            connections.broadcast(authdata3.username(), update, action.getGameID());
            connections.broadcast(null, updateBoardd, action.getGameID());
        }
        catch (InvalidMoveException e) {
            // Handle invalid move
            sendErrorMessage(session, "invalid move can't do that JACK!");
        }
    }

//    private void makeMove(MakeMoveCommand action, Session session) throws IOException {
//
//        String moveData = action.; // Example: "e2 e4"
//        String[] positions = moveData.split(" ");
//
//        String start = positions[0];
//        String end = positions[1];
//
//        // Fetch the game board associated with this session
//        ChessBoard board = connections.getBoard(action.getAuthToken());
//
//        // Attempt to make the move
//        boolean moveSuccess = board.makeMove(start, end); // Assuming `makeMove` exists in ChessBoard
//        if (!moveSuccess) {
//            sendError(session, "Invalid move.");
//            return;
//        }
//
//        // Broadcast the updated board state to all players
//        ServerMessage update = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, "Move made", board);
//        connections.broadcast(action.getAuthToken(), update);
//    }
//
//
//}


//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//}
}