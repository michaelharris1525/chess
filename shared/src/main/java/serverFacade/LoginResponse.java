package serverFacade;

public class LoginResponse {
    private boolean success;
    private String message; // Optional, in case you want a message from the server

    // Getter for success
    public boolean success() {
        return success;
    }

    // Optional: Getter for the message
    public String getMessage() {
        return message;
    }
}
