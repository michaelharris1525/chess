package ui;

public class LoginRequest {
    private final String username;
    private final String password;
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // Getters (no setters needed for this example)
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
