package ui;

import model.GameData;
import requestextension.ResponseException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class PostloginUi {
    private final ServerFacade server;
    //private final ClientNotificationHandler notificationHandler;
    private Collection<GameData> collect;


    public PostloginUi(ServerFacade server) {
        this.server = server;
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
            default -> "Unknown command.";
        };
    }

    private String help() {
        // implement later: join <ID> [WHITE|BLACK] - a game
        return """
                logout - log out of your account
                creategame <game_name> - create a new game
                observe <ID> a game
                listGames - list all existing games
                help - display this help text
                """;
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
        ChessBoard chessBoard = new ChessBoard();
        if(whiteorblack.equalsIgnoreCase("white")){
            chessBoard.renderBoardPerspective(false);
        }
        else {
            chessBoard.renderBoardPerspective(true);
        }
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
            //make a lot more if statements like joining game or observing game
            else{
                System.out.println(help());
                System.out.println("need help? hit any key and hit enter");
            }
        }


    }
}
