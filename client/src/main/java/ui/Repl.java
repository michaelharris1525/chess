package ui;


import requestextension.ResponseException;

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
        System.out.println("If you need help click enter or type help");
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
                    transitionToPostLogin(preloginUi);
                    break; // Exit the loop after switching to Postlogin UI
                } else if (result.equals("quit")) {
                    System.out.println("Goodbye!");
                    break; // Exit the loop to quit the application
                } else {
                    System.out.println(result);
                    System.out.println("If you need help click enter or type help");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye!");
    }

    private void transitionToPostLogin(PreloginUi prelog) throws ResponseException {
        PostloginUi postloginUi = new PostloginUi(prelog.getServerFacade(), prelog.getServerUrl());
        postloginUi.run(); // Call the main functionality of Postlogin UI
    }

    private void printPrompt() {
        System.out.print(">>> ");
    }
}
