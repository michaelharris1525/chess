package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import spark.*;

public class Server {
    private final UserMemorydao userDataobj = new UserMemorydao();
    private final Service serviceobj = new Service();
    private final AuthTokenDataAcess authTokenData = new AuthTokenStorage();
    private final GameDataAccess gameData = new GameStorage();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //Service serviceobj = new Service();
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
                int game_id = serviceobj.creategame(authToken, authTokenData, gameData);
                GameResponse gameResponse = new GameResponse(game_id);
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

        //Clearing it all
        Spark.delete("/db", (Request req, Response res) -> {

            Gson serializer = new Gson();
            try {
                // Call service layer to clear all data, eventually add gamedata here
                serviceobj.clearAllData(userDataobj, authTokenData);

                // If clearing succeeds
                res.status(200);
                return "";

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

//        // Register your endpoints and handle exceptions here.
//        //Spark.get("/path", handler);
//        Spark.post("/user", (Request req, Response Res)->{
//            //turn from json into java object the request needs to be this is deserializing
//            Gson serializer = new Gson();
//            //this turns it into a json
//            UserData userobj = serializer.fromJson(req.body(), UserData.class);
//            //call service layer with userdata
//            AuthData authdata = serviceobj.register(userobj);
//
//            //this returns a string
//            String serailzeaddata = serializer.toJson(authdata);
//            //assigned searadlaisd to the body
//            Res.body(serailzeaddata);
//            //need error paths, need to change the number or serailized error object
//            Res.status(200);
//
//            return serailzeaddata;
//        });

//Delete call for clearing DB
//        Spark.delete("/db", (Request req, Response res) -> {
//            // Handle DELETE request for /db
//            //turn from json into java object the request needs to be this is deserializing
//            Gson serializer = new Gson();
//            //this turns it into a json
//            UserData userobj = serializer.fromJson(req.body(), UserData.class);
//            //call service layer with userdata
//            AuthData authdata = serviceobj.register(userobj);
//
//            //this returns a string
//            String serailzeaddata = serializer.toJson(authdata);
//            //assigned searadlaisd to the body
//            res.body(serailzeaddata);
//            //need error paths, need to change the number or serailized error object
//            res.status(200);
//            return serailzeaddata;
//
//        });