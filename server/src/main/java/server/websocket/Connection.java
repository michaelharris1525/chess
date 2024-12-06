package server.websocket;

import chess.ChessBoard;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public ChessBoard game;

    public Connection(String visitorName, Session session) {
        this.visitorName = visitorName;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
    public ChessBoard getBoard(){
        return game;
    }
}