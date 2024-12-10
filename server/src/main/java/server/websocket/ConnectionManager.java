package server.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ChessGame> games = new ConcurrentHashMap<>(); // Map gameID to ChessGame
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public ChessBoard game = new ChessBoard();

    public void add(String visitorName, Session session, int gameId) {
        var connection = new Connection(visitorName, session,gameId);
        connections.put(visitorName, connection);
        addGame(gameId);
    }
    // Add a new game
    public void addGame(int gameID) {
        games.put(gameID, new ChessGame());
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeVisitorName,
                          ServerMessage notification, int gameId) throws IOException {
        //var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {

            if (c.session.isOpen() && c.gameID == gameId) {
                if (excludeVisitorName == null || !c.visitorName.equals(excludeVisitorName)) {
                    c.send(notification.toString());
                }
            }
        }
    }
}
