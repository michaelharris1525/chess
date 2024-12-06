package server.websocket;

import chess.ChessBoard;
import com.google.gson.Gson;
//import dataaccess.DataAccess;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import webSocketMessages.Action;
//import webSocketMessages.Notification;
import websocket.messages.LoadGame;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> enter(action.getAuthToken(), session);
            case LEAVE -> exit(action.getAuthToken());
        }
    }

    private void enter(String visitorName, Session session) throws IOException {
        connections.add(visitorName, session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(visitorName, notification);
        //make a load game object, copy of board,
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        LoadGame gameM = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,
                null,board);
        String game = new Gson().toJson(gameM);
        //function to grab board
        session.getRemote().sendString(game);
    }

    private void exit(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(visitorName, notification);
    }
}

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