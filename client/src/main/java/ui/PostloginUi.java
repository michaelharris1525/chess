package ui;

import java.util.Arrays;

public class PostloginUi {
    private final ServerFacade server;
    private final ClientNotificationHandler notificationHandler;


    public PostloginUi(ServerFacade server, ClientNotificationHandler notificationHandler) {
        this.server = server;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "logout" -> logout();
            case "help" -> help();
            case "create game" -> createGame(params);
            default -> "Unknown command.";
        };
    }

    private String help() {
        return """
                logout - log out of your account
                create game <game_name> - create a new game
                list games - list all existing games
                help - display this help text
                """;
    }

    private String logout() {
        // Implement logout logic (e.g., call the server's logout API)
        // If login is successful, notify the user and return a signal to transition to the Postlogin UI
        server.logout();
        notificationHandler.notify("Logout successful!");
        return "prelogin"; // Signal to transition to Postlogin UI
    }

    private String createGame(String[] params) {
        // Implement game creation logic (e.g., call the server's create game API)
        return "Game created successfully!";
    }
}
