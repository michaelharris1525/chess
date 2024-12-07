package ui;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.GameData;
import requestextension.ResponseException;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
//import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class InGame {
    private final ServerFacade server;
    private final String serverUrl;
    //private final ClientNotificationHandler notificationHandler;
    private Collection<GameData> collect;
    //after ws
    private WebSocketFacade ws;
    private NotificationHandler notification;
    public InGame(ServerFacade server, String serverUrl) throws ResponseException {
        this.server = server;
        this.serverUrl = serverUrl;
        this.ws = new WebSocketFacade(serverUrl, notification);

        //this.notificationHandler = notificationHandler;
    }
    private String help() {
        // implement later: join <ID> [WHITE|BLACK] - a game
        return """
                Leave (you leave the room but can come back)
                Make_Move - example <B3 ---> B4>
                Resign - you hate losing but you feel like you got no choice
                """;
    }
    public String eval(String input) throws ResponseException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "help" -> help();
            case "leave" -> "quit";
            case "make_move" -> makeMove(params);
            case "load_game" -> loadGame();
            default -> "Unknown command.";
        };
    }

    public String loadGame() throws ResponseException {
        // Prepare and send the UserGameCommand
        ResponseSuccess res = server.getAuth();
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                res.getAuthToken(), server.getCurrentGameId(), null);
        ws.sendMessage(command);
        return "Loading";
    }

    public String makeMove(String[] params) throws ResponseException {
        String chessMoveStartPos = params[0];
        String chessMoveEndPos = params[1];

        // Convert chess notation to row-column indices
        AlphabetToNums abcdefgh = new AlphabetToNums();
        String startColChar = chessMoveStartPos.substring(1,2);
        String endColChar = chessMoveEndPos.substring(1,2);

        int startRow = abcdefgh.getIntfromAlph(chessMoveStartPos.substring(0,1));
        int endRow = abcdefgh.getIntfromAlph(chessMoveEndPos.substring(0,1));

        int startCol = Integer.parseInt(startColChar);
        int endCol = Integer.parseInt(endColChar);

        ChessMove move = new ChessMove(new ChessPosition(startRow, startCol),
                new ChessPosition(endRow, endCol), null);
        // Prepare and send the UserGameCommand
        ResponseSuccess res = server.getAuth();
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,
                        res.getAuthToken(), server.getCurrentGameId(), move);
        ws.sendMessage(command);
//        this.session.getBasicRemote().sendText(new Gson().toJson(moveCommand));
//        System.out.println("Move sent: " + startPos + " to " + endPos);


        return "opponents move";

    }

    public void run() throws ResponseException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Post-login interface! You can start playing now.");
        help();
        String trueornot = " ";
        while (!trueornot.equals("quit")) {
            System.out.print("postlogin>>> ");
            String input = scanner.nextLine();

            String result = eval(input);
            trueornot = result;

            if (result.equals("logout")) {
                System.out.println("Logged out successfully.");
                break; // Exit the Postlogin UI to return to Prelogin
            }
            else if (result.equals("quit")) {
                System.out.println("Quit out Room Successfully!");
                System.exit(0); // Exit the application
            }
            else if(result.equals("opponents move")){
                System.out.println("Move has been made");
            }
            else if(result.equals("Loading")){
                System.out.println("Loading...");
            }
            else{
                System.out.println(help());
                System.out.println("need help? hit any key and hit enter");
            }
        }
    }
}
