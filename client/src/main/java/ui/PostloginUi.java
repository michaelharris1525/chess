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


    public PostloginUi(ServerFacade server) {
        this.server = server;
        //this.notificationHandler = notificationHandler;
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
            default -> "Unknown command.";
        };
    }

    private String help() {
        return """
                logout - log out of your account
                creategame <game_name> - create a new game
                join <ID> [WHITE|BLACK] - a game
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
        if(games != null) {
            for (GameData game : games) {
                System.out.println(game);
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
            //make a lot more if statements like joining game or observing game
            else{
                System.out.println(help());
                System.out.println("need help? hit any key and hit enter");
            }
        }


     }
}
