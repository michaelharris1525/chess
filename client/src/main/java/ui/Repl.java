package ui;


import java.util.Scanner;

import java.util.Scanner;

public class Repl {
    //private final ServerFacade server;
      private final String serverUrl;
//    private final ClientNotificationHandler notificationHandler;

    public Repl(String serverUrl) {
//        this.server = server;
            this.serverUrl = serverUrl;
//        this.notificationHandler = notificationHandler;

    }

    public void run() {
        System.out.println("♔ Welcome to the Chess game! Sign in or register to start.");
        PreloginUi preloginUi = new PreloginUi(serverUrl);
        Scanner scanner = new Scanner(System.in);
        String command = "";

        while (!command.equals("quit")) {
            printPrompt();
            command = scanner.nextLine();

            try {
                String result = preloginUi.eval(command);
                // Check for transition to Postlogin UI
                if (result.equals("postlogin")) {
                    transitionToPostLogin();
                    break; // Exit the loop after switching to Postlogin UI
                } else if (result.equals("quit")) {
                    System.out.println("Goodbye!");
                    break; // Exit the loop to quit the application
                } else {
                    System.out.println(result);
                    System.out.println(" TRY AGAIN");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye!");
    }

    private void transitionToPostLogin() {
        PostloginUi postloginUi = new PostloginUi(serverUrl);
        postloginUi.run(); // Call the main functionality of Postlogin UI
    }

    private void printPrompt() {
        System.out.print(">>> ");
    }

//    private String evaluateCommand(String command) {
//        // Split the command into parts to identify the main command and parameters
//        String[] tokens = command.split(" ");
//        String cmd = tokens[0].toLowerCase();
//        String[] params = (tokens.length > 1) ? Arrays.copyOfRange(tokens, 1, tokens.length) : new String[0];
//
//        return switch (cmd) {
//            case "help" -> displayHelp();
//            case "register" -> client.register(params);
//            case "login" -> client.login(params);
//            case "create" -> client.createGame(params);
//            case "list" -> client.listGames();
//            case "play" -> client.playGame(params);
//            case "logout" -> client.logout();
//            case "quit" -> "quit";
//            default -> "Unknown command. Type 'help' to see available commands.";
//        };
//    }

//    private String displayHelp() {
//        return """
//                Available commands:
//                help - Display this help text
//                register <username> <password> - Register a new account
//                login <username> <password> - Log in to your account
//                create <gameName> - Create a new game
//                list - List all available games
//                play <gameNumber> <color> - Join a game as white or black
//                logout - Log out of your account
//                quit - Exit the application
//                """;
//    }
}
