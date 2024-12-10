package server.websocket;

import chess.ChessBoard;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public ChessBoard game;
    public int gameID; // Add gameID to associate the connection with a game

    public Connection(String visitorName, Session session, int gameID) {
        this.visitorName = visitorName;
        this.session = session;
        this.gameID = gameID; // Initialize gameID
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}