package ui.serverFacade;

public class RegisterRequest {
    private final String realUsername;
    private final String password;
    private final String email;
    public RegisterRequest(String username, String password, String email) {
        this.realUsername = username;
        this.password = password;
        this.email = email;
    }
    // Getters (no setters needed for this example)
    public String getUsername() {
        return realUsername;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
