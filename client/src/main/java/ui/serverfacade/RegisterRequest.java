package ui.serverfacade;

public class RegisterRequest {
    private final String username;
    private final String password;
    private final String email;
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    // Getters (no setters needed for this example)
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
