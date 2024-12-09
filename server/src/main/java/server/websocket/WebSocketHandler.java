package server.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
//import dataaccess.DataAccess;
import dataaccess.AuthSQLTokenClass;
import dataaccess.GameSQLDao;
import dataaccess.GameStorage;
import dataaccess.UserSQLDao;
import model.GameData;
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


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameSQLDao gameDao = new GameSQLDao();
    private AuthSQLTokenClass authDao = new AuthSQLTokenClass();
    private UserSQLDao userDao = new UserSQLDao();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, InvalidMoveException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> {
                if(enterGameAttempt(action.getGameID(), action.getAuthToken()) == true){
                    enterGameSuccesful(action.getAuthToken(), session, action.getGameID());
                }
                else {
                    sendErrorMessage(session, "gameId or your input is wrong");
                }
            }

            case LEAVE -> exit(action.getAuthToken());
            //case MAKE_MOVE -> makeMove(action, session);
            case MAKE_MOVE -> {
                makeMove(action, session);
            }
            case RESIGN -> resign(action.getAuthToken(), session);
        }
    }

    private void resign(String authToken, Session session) throws IOException {
        connections.remove(authToken);
        //userDao.get
        var message = String.format("%s has resigned the game", authToken);
        var notification = new Notifications(message);
        connections.broadcast(authToken, notification);
    }

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

    private void enterGameSuccesful(String visitorName, Session session, int gameId) throws IOException {
        connections.add(visitorName, session);
        //userDao.get
        var message = String.format("%s is in the game auth token for now", visitorName);
        var notification = new Notifications(message);
        connections.broadcast(visitorName, notification);
        //make a load game object, copy of board, change later to get
        //the actual board that is being played at its current state
        ChessGame gamegame = gameDao.getGameData(gameId).game();
        ChessBoard board = gamegame.getBoard();
        LoadGame gameM = new LoadGame(board);
        String game = new Gson().toJson(gameM);
        //function to grab board
        session.getRemote().sendString(game);
    }

    private void exit(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notifications(message);
        connections.broadcast(visitorName, notification);
    }

    private void makeMove(UserGameCommand action, Session session) throws IOException, InvalidMoveException {
        // Extract the realMove from the command
        try {
            GameData gameData = gameDao.getGameData(action.getGameID());
            if (gameData == null) {
                System.out.println("Game not found.");
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

            // Attempt to make the move on the board
            gameData.game().makeMove(move);
            board = gameData.game().getBoard();
            gameData.game().setBoard(board);

            // Broadcast the updated board state to all players
            LoadGame updateBoardd = new LoadGame(board);
            String toUserBoard = new Gson().toJson(updateBoardd);
            Notifications update = new Notifications(
                    "Move made");
            //send the message of it being updated to the users
            connections.broadcast(action.getAuthToken(), update);
            connections.broadcast(null, updateBoardd);
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