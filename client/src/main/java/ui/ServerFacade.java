package ui;

import com.google.gson.Gson;
import model.GameData;
import requestextension.ResponseException;
import ui.serverFacade.RegisterRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    //Part 1

    public boolean login(String username, String password) throws ResponseException {
        var path = "/session"; // Server endpoint for login
        // Create a request object to send to the server
        var loginRequest = new LoginRequest(username, password);
        // Make a POST request to the server
        var response = this.makeRequest("POST", path, loginRequest, ResponseSuccess.class);
        // Check if the login response indicates success (e.g., a boolean or status message)
        return response != null && response.success();
    }
    public boolean register(String username, String password, String email) throws ResponseException {
        var path = "/user"; // Server endpoint for login
        // Create a request object to send to the server
        var registerRequest = new RegisterRequest(username, password, email);
        // Make a POST request to the server
        var response = this.makeRequest("POST", path, registerRequest, ResponseSuccess.class);
        // Check if the login response indicates success (e.g., a boolean or status message)
        return response != null && response.success();
    }

    //part 2
    public void logout(){
        var path = "/session";

        try {
            this.makeRequest("DELETE", path, null, null);
        }
        catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    public void clientuserCreateGame(String gameName) throws ResponseException {
        var path = "/game";
        this.makeRequest("POST", path, gameName, GameData.class);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            String reqData = new Gson().toJson(request);

            writeBody(request, http);
            System.out.println("Request URL: " + (serverUrl + path));
            System.out.println("Request Body: " + reqData);
            http.connect();
//            if(method == "DELETE")
//            {
//                return null;
//
//            }
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }



}
