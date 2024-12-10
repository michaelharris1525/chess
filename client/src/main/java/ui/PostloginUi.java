package ui;

import chess.ChessBoard;
import model.GameData;
import requestextension.ResponseException;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;


public class PostloginUi {
    private final ServerFacade server;
    private final String serverUrl;
    //private final ClientNotificationHandler notificationHandler;
    private Collection<GameData> collect;
    //after ws
    private WebSocketFacade ws;
    private NotificationHandler notification;


    public PostloginUi(ServerFacade server, String serverUrl) {
        this.server = server;
        this.serverUrl = serverUrl;

        //this.notificationHandler = notificationHandler;
    }
    //storage
    private void keepMap(Collection<GameData> c){
        this.collect = c;
    }
    private Collection<GameData> keepMap(){
        return this.collect;
    }


    public String eval(String input) throws ResponseException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "logout" -> logout();
            case "help" -> help();
            case "creategame" -> createGame(params);
            case "listgames" -> listAllGames();
            case "observegame" ->observeGame(params);
            case "join" -> joinGame(params);
            default -> "Unknown command.";
        };
    }

    private String help() {
        // implement later: join <ID> [WHITE|BLACK] - a game
        return """
                logout - log out of your account
                creategame <game_name> - create a new game
                join <ID> <White|Black> - a game
                observegame <ID> <white|black> - a game
                listGames - list all existing games
                help - display this help text
                """;
    }
    public String joinGame(String[] params) throws ResponseException {
        if (params.length != 2) {
            throw new ResponseException(400, "Invalid input. Use: join <ID> <White|Black>");
        }
        int gameId = Integer.parseInt(params[0]);
        String whiteBlack = params[1];

        if (!whiteBlack.equalsIgnoreCase("WHITE") &&
                !whiteBlack.equalsIgnoreCase("BLACK")) {
            throw new ResponseException(400, "Team choice must be either 'White' or 'Black'.");
        }


        // Make the HTTP call to join the game
        server.joinGame(server.getAuth(), gameId, whiteBlack);

        // Establish the WebSocket connection
        ws = new WebSocketFacade(serverUrl, notification);
        ws.connectToGame(server.getAuth(), gameId, whiteBlack);

        // Print a message for confirmation
        System.out.println("Joined game " + params[0] + " as " + whiteBlack + ".");

        return "inGame";
        //Call the server join HTTP API to join them to the game.
        //This step is only done for players. Observers do not need to make the join HTTP API request.
        //is it the correct gameID? Does it match with the DatabasesID?
        //white or black available? if so, call server for websocket connection with params
    }

    private String logout() {
        // Implement logout logic (e.g., call the server's logout API)
        // If login is successful, notify the user and return a signal to transition to the Postlogin UI
        server.logout();
        System.out.println("Logout successful!");
        return "prelogin"; // Signal to transition to Postlogin UI
    }

    private String listAllGames() throws ResponseException {
        Map<String, Collection<GameData>> listofAllGames = server.flistAllGames();
        Collection<GameData>games = listofAllGames.get("games");
        keepMap(games);
        if(games != null) {
            for (GameData game : games) {
                if(game.whiteUsername() != null && game.blackUsername() != null) {
                    System.out.println(game.gameID() + ": " + game.gameName() + "Only can Observe");
                }
                else if (game.whiteUsername() == null && game.blackUsername() != null) {
                    System.out.println(game.gameID() + ": " + game.gameName() + "White Available");
                }
                else if(game.blackUsername() == null && game.whiteUsername() != null){
                    System.out.println(game.gameID() + ": " + game.gameName() + "Black Available");
                }
                else if(game.blackUsername() == null && game.whiteUsername() == null){
                    System.out.println(game.gameID() + ": " + game.gameName() + "White|Black");
                }
            }
        }
        return "listgames";
    }

    private String createGame(String[] params) throws ResponseException {
        // Implement game creation logic (e.g., call the server's create game API)
        System.out.println("CREATED GAME successful!");
        String gameName = params[0];
        server.clientuserCreateGame(gameName);
        return "creategame";
    }
    private String observeGame(String[] params) throws ResponseException {
        String iD = params[0];
        String whiteorblack = params[1];
        int intyID = Integer.parseInt(iD);

        ws = new WebSocketFacade(serverUrl, notification);
        ws.connectToGame(server.getAuth(), intyID, null);

        // Fetch initial game state from server
        //ChessBoard currentBoard = fetchGameState(gameIDInt); // Method to get the initial board state from the server

        // Display the board
//        DisplayChessBoard displayBoard = new DisplayChessBoard(currentBoard);
//        if(whiteorblack.equalsIgnoreCase("white")){
//            displayBoard.renderBoardPerspective(false);
//        }
//        else{
//            displayBoard.renderBoardPerspective(true);
//        }
        // Listen for updates
//        ws.onMessage(message -> {
//            updateBoardState(currentBoard, message); // Update local board based on message
//            displayBoard.renderBoardPerspective(false); // Re-render after update
//        });
        //delete later
//        ChessBoard board = new ChessBoard();
//        board.resetBoard();
//        DisplayChessBoard chessBoard = new DisplayChessBoard(board);

        //do Websocket don't do http request
//        if(whiteorblack.equalsIgnoreCase("white")){
//            chessBoard.renderBoardPerspective(false);
//        }
//        else {
//            chessBoard.renderBoardPerspective(true);
//        }
        //do Websocket don't do http request
        server.observeID(intyID);
        return "observe game";
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
                System.out.println("Goodbye!");
                System.exit(0); // Exit the application
            }
            else if (result.equals("creategame")) {
                System.out.println("made game successful!");
            }
            else if (result.equals("listgames")) {
                System.out.println("Join A GAME!");
            }
            else if(result.equals("observe game")){
                System.out.println("You are now observing game!");
            }
            else if(result.equals("inGame")){
                transitionToInGame();
            }
            //If joingame go to new class, we'll call it InGame with new functions and if statements to do
            //make a lot more if statements like joining game or observing game
            else{
                System.out.println(help());
                System.out.println("need help? hit any key and hit enter");
            }
        }
    }

    private void transitionToInGame() throws ResponseException {
        InGame ingame = new InGame(server, serverUrl);
        ingame.run(); // Call the main functionality of InGame UI
    }
}
