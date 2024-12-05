package ui;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.GameData;
import requestextension.ResponseException;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
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
    public InGame(ServerFacade server, String serverUrl) {
        this.server = server;
        this.serverUrl = serverUrl;

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
            case "Leave" -> "quit";
            case "Make_Move" -> makeMove(params);
            default -> "Unknown command.";
        };
    }

    public String makeMove(String[] params){
        String chessMoveStartPos = params[0];
        String chsssMoveEndPos = params[1];

        // Convert chess notation to row-column indices
        int startCol = 8 - Character.getNumericValue(chessMoveStartPos.charAt(1));  // Rows are 1-8 in reverse
        int startRow = chessMoveStartPos.charAt(0) - 'A';  // 'A' maps to 0, 'B' to 1, etc.

        int endCol = 8 - Character.getNumericValue(chsssMoveEndPos.charAt(1));
        int endRow = chsssMoveEndPos.charAt(0) - 'A';

        ChessMove move = new ChessMove(new ChessPosition(startRow, startCol),
                new ChessPosition(endRow, endCol), null);
        // Prepare and send the UserGameCommand
        ResponseSuccess res = server.getAuth();
        UserGameCommand command = new UserGameCommand
                (UserGameCommand.CommandType.MAKE_MOVE,
                        res.getAuthToken(), server.getCurrentGameId());
        //ws.sendMessage(new Gson().toJson(command));
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
            else{
                System.out.println(help());
                System.out.println("need help? hit any key and hit enter");
            }
        }
    }
}
