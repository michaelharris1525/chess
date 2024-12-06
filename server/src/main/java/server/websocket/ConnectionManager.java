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
    public void add(String visitorName, Session session) {
        var connection = new Connection(visitorName, session);
        connections.put(visitorName, connection);
    }
    // Add a new game
    public void addGame(int gameID) {
        games.put(gameID, new ChessGame());
    }
    // Get the game by gameID
    public ChessGame getGame(int gameID) {
        return games.get(gameID);
    }
    //update board everytime there is a connection
    public void addWithBoard(String visitorName, Session session, ChessBoard game) {
        var connection = new Connection(visitorName, session);
        connection.game = game; // Assign the game board to the connection
        connections.put(visitorName, connection);
    }

    public ChessBoard getBoard(String visitorName) {
        Connection connection = connections.get(visitorName);
        if (connection != null) {
            return connection.game;
        } else {
            return null;
        }
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}
