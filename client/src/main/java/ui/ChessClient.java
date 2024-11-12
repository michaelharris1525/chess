package ui;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import requestextension.ResponseException;
import serverFacade.ServerFacade;

import java.util.Arrays;

public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    //private WebSocketFacade ws;
    //private State state = State.SIGNEDOUT;

    public ChessClient(ServerFacade server, String serverUrl, NotificationHandler notificationHandler) {
        this.server = server;
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }
}
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signin" -> signIn(params);
                case "list" -> listPets();
                case "signout" -> signOut();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    }

    public String signIn(String... params) throws ResponseException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.enterPetShop(visitorName);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException(400, "Expected: <yourname>");
    }
    public String signOut() throws ResponseException {
        assertSignedIn();
        ws.leavePetShop(visitorName);
        ws = null;
        state = State.SIGNEDOUT;
        return String.format("%s left the shop", visitorName);
    }

    public String listPets() throws ResponseException {
        assertSignedIn();
        var pets = server.listPets();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var pet : pets) {
            result.append(gson.toJson(pet)).append('\n');
        }
        return result.toString();
    }
    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                        - signIn <yourname>
                        - quit
                        """;
        }
        return """
                    - list
                    - adopt <pet id>
                    - rescue <name> <CAT|DOG|FROG|FISH>
                    - adoptAll
                    - signOut
                    - quit
                    """;
    }

}
