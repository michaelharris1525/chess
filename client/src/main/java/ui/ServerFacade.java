package ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.AuthData;
import model.GameData;
import requestextension.ResponseException;
import ui.serverFacade.CreateGameReq;
import ui.serverFacade.ListGameReq;
import ui.serverFacade.RegisterRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServerFacade {

    private final String serverUrl;
    private ResponseSuccess resAuthToken;
    public ServerFacade(String url) {
        serverUrl = url;
    }


    private void keepAuthToken(ResponseSuccess res){
        this.resAuthToken = res;
    }
    public ResponseSuccess getAuth(){
        return this.resAuthToken;
    }
    //part 1
    public ResponseSuccess login(String username, String password) throws ResponseException {
        var path = "/session"; // Server endpoint for login
        // Create a request object to send to the server
        var loginRequest = new LoginRequest(username, password);
        // Make a POST request to the server
        ResponseSuccess response = this.makeRequest("POST", path, loginRequest, ResponseSuccess.class);
        keepAuthToken(response);
        return response;
        // Check if the login response indicates success (e.g., a boolean or status message)
        //return response != null && response.success();
    }
    public ResponseSuccess register(String username, String password, String email) throws ResponseException {
        var path = "/user"; // Server endpoint for login
        // Create a request object to send to the server
        var registerRequest = new RegisterRequest(username, password, email);
        // Make a POST request to the server
        ResponseSuccess response = this.makeRequest("POST", path, registerRequest, ResponseSuccess.class);
        keepAuthToken(response);
        return response;
        // Check if the login response indicates success (e.g., a boolean or status message)
        //return response != null && response.success();
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
    public  Map<String, Collection<GameData>>  flistAllGames() throws ResponseException {
        var path ="/game";
        Map<String, Collection<GameData>> responseObj = new HashMap<>();

        //var response = this.makeRequest("GET", path, null, responseObj);

        // Make a GET request to the server and deserialize the response
        String jsonResponse = this.makeRequest("GET", path, null, String.class);

        // Deserialize the JSON response into the map structure
        return new Gson().fromJson(jsonResponse,new TypeToken<Map<String, Collection<GameData>>>() {}.getType());
    }
    //var createGameRequest = new ListGameReq(gameName);
    public void clientuserCreateGame(String gameName) throws ResponseException {
        var path = "/game";
        // Create a request object to send to the server
        var createGameRequest = new CreateGameReq(gameName);
        this.makeRequest("POST", path, createGameRequest, GameData.class);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            ResponseSuccess getAuth = getAuth();
            //everything but login and register
            if(getAuth != null) {
                http.addRequestProperty("authorization", getAuth.getAuthToken());
            }
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