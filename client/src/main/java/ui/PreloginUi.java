package ui;

import requestextension.ResponseException;

import java.util.Arrays;

public class PreloginUi {
//    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    //private final ClientNotificationHandler notificationHandler;

    public PreloginUi(String serverUrl) {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
    }

    public String eval(String input) throws ResponseException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> signIn(params);
            case "quit" -> "quit";
            case "help" -> help();
            default -> help();
        };
    }

    private String help() {
        return """
                    register <username> <passwoerd> <email> - create an account
                    login <userName> <passWord> - to play chess
                    quit - exit
                    help - with possible commands
                    """;

    }

    private String register(String[] params) throws ResponseException {
        if (params.length < 2) {
            return "Error: You must provide both username and password to login.";
        }
        String userName = params[0];
        String passWord = params[1];
        String email = params[2];

        // Call the ServerFacade to attempt login
        boolean success = server.register(userName, passWord,email);

        if (success) {
            // If login is successful, notify the user and return a signal to transition to the Postlogin UI
            //notificationHandler.notify("Register successful!");
            System.out.println("Register successful!");

            return "postlogin"; // Signal to transition to Postlogin UI
        } else {
            // If login fails, notify the user and return an error message
            //notificationHandler.notify("Error: Invalid username or password.");
            System.out.println("FAILED TRY AGAIN LOSER!");

            return "prelogin"; // Stay in the Prelogin UI (optional, can be customized)
        }

    }

    private String signIn(String[] params) throws ResponseException {
        if (params.length < 2) {
            return "Error: You must provide both username and password to login.";
        }
        String userName = params[0];
        String passWord = params[1];

        // Call the ServerFacade to attempt login
        boolean success = server.login(userName, passWord);

        if (success) {
            // If login is successful, notify the user and return a signal to transition to the Postlogin UI
            System.out.println("LOGIN successful!");
            return "postlogin"; // Signal to transition to Postlogin UI
        } else {
            // If login fails, notify the user and return an error message
            //notificationHandler.notify("Error: Invalid username or password.");
            System.out.println("LOSER! Error: Invalid username or password.");
            return "prelogin"; // Stay in the Prelogin UI (optional, can be customized)
        }

    }
    public ServerFacade getServerFacade(){
        return this.server;
    }
}
