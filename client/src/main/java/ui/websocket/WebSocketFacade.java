package ui.websocket;

import chess.ChessBoard;
import model.GameData;
import ui.DisplayChessBoard;
import com.google.gson.Gson;
import requestextension.ResponseException;
import ui.ResponseSuccess;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.ServerMessage;
//actions = game commands, notifications = servermessage

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);




            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    //delete later
                    System.out.println("Received message: " + message);

                    ServerMessage notification = new Gson().
                            fromJson(message, ServerMessage.class);

                    if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        handleServerMessage(notification, null);
                    }
                    else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                        LoadGame loadingBoard = new Gson().
                                fromJson(message, LoadGame.class);
                        handleServerMessage(notification, loadingBoard);
                    }
                    else {
                        handleServerMessage(notification, null);
                    }
                    // Handle game updates like moves or board state changes
                    //handleServerMessage(notification);



                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connectToGame(ResponseSuccess userObj, int gameId) throws ResponseException {
        try {
            // Validate inputs
            if (userObj == null || userObj.getAuthToken() == null || gameId <= 0) {
                throw new ResponseException(400, "Invalid authToken or gameId.");
            }
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.
                    CONNECT, userObj.getAuthToken(), gameId, null);
            //this.session.getBasicRemote().sendText(new Gson().toJson(action));

            // Ensure WebSocket session is open before sending
            if (session.isOpen()) {
                this.session.getBasicRemote().sendText(new Gson().toJson(action));
                System.out.println("Connected to game with ID: " + gameId);
            } else {
                throw new ResponseException(500, "WebSocket session is not open.");
            }
        }
        catch(IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }
//    private void updateGameBoard(String boardJson) {
//        ChessBoard updatedBoard = new Gson().fromJson(boardJson, ChessBoard.class);
//        displayChessBoard.updateBoard(updatedBoard);
//        displayChessBoard.renderBoardPerspective(false);  // Example perspective
//    }

    private void handleServerMessage(ServerMessage notification, LoadGame game) {
        switch (notification.getServerMessageType()) {
//                break; DO NOT NEED GAME OVER JUST GO THROUGH NOTIFICATION
            case LOAD_GAME:
                //Load the Game here, update the board
                //update the chess board
                //updateGameBoard(notification.getMessage());
                //DisplayChessBoard displayBoard = new DisplayChessBoard(currentBoard);
                //LoadGame gameMessage = (LoadGame) notification;
                ChessBoard board = game.getGame();
                DisplayChessBoard displayBoard = new DisplayChessBoard(board);
                displayBoard.renderBoardPerspective(false);
                break;

            case NOTIFICATION:
                // Handle notifications like player joining or moves
                notificationHandler.notify(notification);
                break;

            case ERROR:
                System.err.println("Error: " + notification);
                break;

            default:
                System.out.println("Unknown message type.");
                break;
        }
    }

    public void sendMessage(UserGameCommand command) throws ResponseException {
        try {
            if (session.isOpen()) {
                String message = new Gson().toJson(command);
                this.session.getBasicRemote().sendText(message);
                System.out.println("Sent message: " + message);
            } else {
                throw new ResponseException(500, "WebSocket session is not open.");
            }
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}

