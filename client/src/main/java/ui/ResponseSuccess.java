package ui;

public class ResponseSuccess {
    private final boolean success = true;
    private String authToken;
    private String user;

    public ResponseSuccess(String authToken, String user){
        this.authToken = authToken;
        this.user = user;
    }

    // Getter for success
    public boolean success() {
        return this.success;
    }

}
