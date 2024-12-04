package ui.websocket;

import com.google.gson.Gson;
import requestextension.ResponseException;
import ui.ResponseSuccess;
import websocket.commands.UserGameCommand;
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
                    //change here from notifications to server message
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);

                    // Handle game updates like moves or board state changes
                    handleServerMessage(notification);

                    //loading THE BOARD MAKING MOVE


                    //loading the board if user prompts it


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
            var action = new UserGameCommand(UserGameCommand.CommandType.
                    CONNECT, userObj.getAuthToken(), gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        }
        catch(IOException ex){
            throw new ResponseException(500, ex.getMessage());
        }

    }

    private void handleServerMessage(ServerMessage notification) {
        // Logic to handle different types of messages, e.g., moves or board updates
        if (notification.getServerMessageType() == ServerMessage.ServerMessageType.MAKE_MOVE) {
            // Update chessboard with the move data
            ChessBoard.updateWithMove(notification.getMoveData());
        } else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.GAME_OVER) {
            // Handle game over scenario
            handleGameOver();
        }
    }

//    public void enterPetShop(String visitorName) throws ResponseException {
//        try {
//            var action = new Action(Action.Type.ENTER, visitorName);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
//        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//
//    public void leavePetShop(String visitorName) throws ResponseException {
//        try {
//            var action = new Action(Action.Type.EXIT, visitorName);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
//            this.session.close();
//        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }

}

