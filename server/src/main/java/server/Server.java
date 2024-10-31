package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import spark.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dataaccess.DatabaseManager;
import dataaccess.UserSQLDao;


public class Server {
    private final UserMemorydao userDataobj = new UserMemorydao();
    private final Service serviceobj = new Service();
    private final AuthTokenDataAcess authTokenData = new AuthTokenStorage();
    private final GameDataAccess gameData = new GameStorage();

    public void runServer() {
        try {
            DatabaseManager.createDatabase();
            // Create tables if they don't exist
            DatabaseManager.createTables();
            // Start your services/endpoints
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int run(int desiredPort) {
        runServer();
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        //registration
        Spark.post("/user", (Request req, Response res) -> {
            Gson serializer = new Gson();
            try {
                // Deserialize the request to UserData object
                UserData userObj = serializer.fromJson(req.body(), UserData.class);

                // Validate user input (e.g., check username and password and gmail are not blank
                if (userObj.username() == null || userObj.password() == null || userObj.email() == null) {
                    res.status(400); // Bad request
                    return serializer.toJson(new ErrorData("Error: bad request"));
                }
                //Validate if the user is in the database or not
                //is this register(RegisterRequest) Handler side?

                // Call service layer to register the user
                AuthData authData = serviceobj.register(userObj, userDataobj, authTokenData);


                // If registration succeeds
                res.status(200);
                return serializer.toJson(authData);

            }
            catch (UserAlreadyExistsException e) {
                // Handle user already exists error
                res.status(403); // Forbidden
                return serializer.toJson(new ErrorData("Error: already taken"));
            }
            catch (Exception e) {
                // Handle generic exception for bad JSON format or other errors, no need for this but its alright
                if (e instanceof com.google.gson.JsonSyntaxException) {
                    res.status(400); // Bad request for invalid JSON
                    return serializer.toJson(new ErrorData("Invalid JSON format."));
                } else {
                    // Handle any other server-side error
                    res.status(500); // Internal Server Error
                    return serializer.toJson(new ErrorData("Error: (description of error)"));
                }
            }
        });

        //Login in User
        Spark.post("/session", (Request req, Response res) -> {
            Gson serializer = new Gson();
            try {
                // Deserialize the request to UserData object
                UserData userObj = serializer.fromJson(req.body(), UserData.class);

                // Validate user input (e.g., check username and password are not blank
                if (userObj.username() == null || userObj.password() == null) {
                    res.status(400); // Bad request
                    return serializer.toJson(new ErrorData("Error: bad request"));
                }

                // Call service layer to register the user
                AuthData authData = serviceobj.loginuser(userObj, userDataobj, authTokenData);


                // If login succeeds
                res.status(200);
                return serializer.toJson(authData);

            }
            catch (UserNameIsNullinMemoryDao e) {
                // Handle user already exists error
                res.status(401); // Forbidden
                return serializer.toJson(new ErrorData("Error: UserName is wrong"));
            }
            catch (UserNameIsWrong e) {
                // Handle user already exists error
                res.status(401); // Forbidden
                return serializer.toJson(new ErrorData("Error: UserName is wrong"));
            }
            catch (UserPasswordIsWrong e) {
                // Handle user already exists error
                res.status(401); // Forbidden
                return serializer.toJson(new ErrorData("Error: UserPassword is wrong"));
            }
            catch (Exception e) {
                // Handle generic exception for bad JSON format or other errors, no need for this but its alright
                if (e instanceof com.google.gson.JsonSyntaxException) {
                    res.status(400); // Bad request for invalid JSON
                    return serializer.toJson(new ErrorData("Invalid JSON format."));
                } else {
                    // Handle any other server-side error
                    res.status(500); // Internal Server Error
                    return serializer.toJson(new ErrorData("Error: (description of error)"));
                }
            }
        });
        //logout
        Spark.delete("/session", (Request req, Response res) -> {
            Gson serializer = new Gson();
            try {
                //authToken could use a string for future reference
                //AuthData auth = new AuthData(req.headers("authorization"), null);

                serviceobj.logout(req.headers("authorization"), authTokenData);

                // If login succeeds
                res.status(200);
                return serializer.toJson(new Object());


            }
            catch (DataAccessException e) {
                // Handle any other server-side error
                res.status(401); // Internal Server Error
                return serializer.toJson(new ErrorData("Error: Unable to clear data."));
            }

            catch (Exception e) {
                // Handle any other server-side error
                res.status(500); // Internal Server Error
                return serializer.toJson(new ErrorData("Error: Unable to clear data."));
            }
        });


        //Create Game two extra things to do here is to make 2 exceptions on request could be null, and auth token to be null
        Spark.post("/game", (Request req, Response res) -> {
            Gson serializer = new Gson();
            try {
                //AuthData auth = new AuthData(req.headers("authorization"), null);
                String authToken = req.headers("authorization");
                if(authToken == null) {
                    throw new BadRequestsException("Bad request exception");
                }
                //extract game name to add to change
                Map<String, String> requestBody =
                        serializer.fromJson(req.body(), Map.class);
                String nameOfGameFromUser = requestBody.get("gameName");
                int gameId = serviceobj.createGame(authToken, authTokenData, gameData, nameOfGameFromUser);
                GameResponse gameResponse = new GameResponse(gameId);
                // If create game succeeds
                res.status(200);
                return serializer.toJson(gameResponse);
            }
            catch (BadRequestsException e) {
                // Handle any other server-side error
                res.status(401); // Internal Server Error
                return serializer.toJson(new ErrorData("Error: BadRequestsexception"));
            }
            catch (DataAccessException e) {
                // Handle any other server-side error
                res.status(401); // Internal Server Error
                return serializer.toJson(new ErrorData("Error: BadAuthentication"));
            }
            catch (Exception e) {
                // Handle any other server-side error
                res.status(500); // Internal Server Error
                return serializer.toJson(new ErrorData("Error: Unable to clear data."));
            }

        });

        //List Games
        Spark.get("/game", (Request req, Response res) -> {
            Gson serializer = new Gson();
            try {
                String authToken = req.headers("authorization");
                if (authToken == null || !authTokenData.containsAuthToken(authToken)) {
                    throw new BadRequestsException("Bad request exception");
                }
                // Collect all games and format them
                Collection<GameData>gameDatasets = gameData.getAllGameData();

                // Create the response structure
                Map<String, Collection<GameData>> response = new HashMap<>();
                response.put("games", gameDatasets);

                res.status(200);
                return serializer.toJson(response);
            }
            catch (BadRequestsException e) {
                // Handle any other server-side error
                res.status(401); // Internal Server Error
                return serializer.toJson(new ErrorData("Error: BadRequestsexception"));
            }
            catch (Exception e) {
                // Handle any other server-side error
                res.status(500); // Internal Server Error
                return serializer.toJson(new ErrorData("Error: Unable to clear data."));
            }
        });

        //JOINING GAME
        Spark.put("/game", (Request req, Response res) -> {
                Gson serializer = new Gson();
                try {
                    //AuthData auth = new AuthData(req.headers("authorization"), null);
                    String authToken = req.headers("authorization");
                    if (authToken == null || !authTokenData.containsAuthToken(authToken)) {
                        throw new BadRequestsException("Bad request exception");
                    }

                    JoinGameRequest joinRequest = serializer.fromJson(req.body(), JoinGameRequest.class);

                    if (joinRequest == null || joinRequest.getPlayerColor() == null
                            || joinRequest.getGameID() == null) {
                        throw new JoinRequestIsNull("JoinREQUEST IS NULL");
                    }

                    //Retrieve game and validate the color choice
                    GameData gameDATAFROMREQUEST =  gameData.getGameData(joinRequest.getGameID());

                    serviceobj.joinGame(authToken, authTokenData, gameData, gameDATAFROMREQUEST, joinRequest);

                    // If create game succeeds
                    res.status(200);
                    //return serializer.toJson(new Object());
                    return "{}";
                }
                catch(JoinRequestIsNull e){
                    res.status(400); // Internal Server Error
                    return serializer.toJson(new ErrorData("Error: BadRequestsexception"));
                }
                catch (BadRequestsException e) {
                    // Handle any other server-side error
                    res.status(401); // Internal Server Error
                    return serializer.toJson(new ErrorData("Error: BadRequestsexception"));
                }
                catch (DataAccessException e) {
                    // Handle any other server-side error
                    res.status(401); // Internal Server Error
                    return serializer.toJson(new ErrorData("Error: unauthorized"));
                }
                catch (PlayerColorException e){
                    res.status(403); // Internal Server Error
                    return serializer.toJson(new ErrorData("Error: unauthorized"));
                }
                catch (Exception e) {
                    // Handle any other server-side error
                    res.status(500); // Internal Server Error
                    return serializer.toJson(new ErrorData("Error: Unable to clear data."));
                }
            });


        //Clearing it all
        Spark.delete("/db", (Request req, Response res) -> {

            Gson serializer = new Gson();
            try {
                // Call service layer to clear all data, eventually add gamedata here
                serviceobj.clearAllData(userDataobj, authTokenData,gameData);

                // If clearing succeeds
                res.status(200);
                return "{}";

            } catch (Exception e) {
                // Handle any other server-side error
                res.status(500); // Internal Server Error
                return serializer.toJson(new ErrorData("Error: Unable to clear data."));
            }
        });



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
