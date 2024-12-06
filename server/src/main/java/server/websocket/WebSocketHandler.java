package server.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
//import dataaccess.DataAccess;
import dataaccess.GameSQLDao;
import dataaccess.GameStorage;
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
    private GameSQLDao getDao = new GameSQLDao();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, InvalidMoveException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> {
                if(enterGameAttempt(action.getGameID()) == true){
                    enterGameSuccesful(action.getAuthToken(), session);
                }
                else {
                    sendErrorMessage(session);
                }
            }

            case LEAVE -> exit(action.getAuthToken());
            //case MAKE_MOVE -> makeMove(action, session);
            case MAKE_MOVE -> {
                makeMove(action, session);

            }
        }
    }

    private void sendErrorMessage(Session session) throws IOException {
        ErrorMessage m  = new ErrorMessage("Error: You messed up the game key Id");
        String error = new Gson().toJson(m);
        session.getRemote().sendString(error);
    }

    private boolean enterGameAttempt(int gameId){
        //check game Id is correct
            if(getDao.gameExists(gameId)){
                return true;
            }
            else{
                return false;
            }
    }

    private void enterGameSuccesful(String visitorName, Session session) throws IOException {
        connections.add(visitorName, session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new Notifications(message);
        connections.broadcast(visitorName, notification);
        //make a load game object, copy of board, change later to get
        //the actual board that is being played at its current state
        ChessBoard board = new ChessBoard();
        board.resetBoard();
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
        ChessGame game = connections.getGame(action.getGameID());
        if (game == null) {
            System.out.println("Game not found.");
            return;
        }
        ChessMove move = action.getRealMove();

        // Fetch the game board associated with the gameID or authToken
        ChessBoard board = connections.getBoard(action.getAuthToken());
        if (board == null) {
            System.out.println("Game not found for the given authToken.");
            return;
        }

        // Attempt to make the move on the board
        game.makeMove(move);

        // Broadcast the updated board state to all players
        LoadGame updateBoardd = new LoadGame(board);
        Notifications update = new Notifications(
                "Move made");
        //send the message of it being updated to the users
        connections.broadcast(action.getAuthToken(), update);
        connections.broadcast(action.getAuthToken(),updateBoardd);
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