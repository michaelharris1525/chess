package serverFacade;

public class LoginRequest {
    private final String realUsername;
    private final String password;
    public LoginRequest(String username, String password) {
        this.realUsername = username;
        this.password = password;
    }
    // Getters (no setters needed for this example)
    public String getUsername() {
        return realUsername;
    }

    public String getPassword() {
        return password;
    }
}
