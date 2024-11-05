package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import requestextension.JoinRequestIsNull;
import spark.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import dataaccess.UserSQLDao;

public class Server {
    private final UserSQLDao userDataobj = new UserSQLDao();
    private final Service serviceobj = new Service();
    private final AuthSQLTokenClass authTokenData = new AuthSQLTokenClass();
    private final GameSQLDao gameData = new GameSQLDao();

    //SQL Databases
    private final UserSQLDao userSQL = new UserSQLDao();
    private final GameSQLDao gameSQLDAO = new GameSQLDao();
    private final AuthSQLTokenClass authSQL = new AuthSQLTokenClass();

    //Service Helper
    private final ServerHelper serverFunctions = new ServerHelper();

    private String handleException(Exception ex, Response res, Gson serializer) {
        if (ex instanceof com.google.gson.JsonSyntaxException) {
            res.status(400); // Bad request for invalid JSON
            return serializer.toJson(new ErrorData("Invalid JSON format."));
        } else {
            res.status(500); // Internal Server Error
            return serializer.toJson(new ErrorData("Error: (description of error)"));
        }
    }
    public void setUpServer() {
        try {
            // Create tables if they don't exist
            userSQL.userconfigureDatabase();
            gameSQLDAO.gameconfigureDatabase();
            authSQL.authconfigureDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int run(int desiredPort) {
        setUpServer();
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        //registration
        Spark.post("/user", (Request req, Response res) -> {
            Gson serializer = new Gson();
            return serverFunctions.userRegistrationHelp(req,res,serializer);
        });
        //Login in User
        Spark.post("/session", serverFunctions::userLogin);

        //logout
        Spark.delete("/session", serverFunctions::userLogout);

        //Create Game
        Spark.post("/game", serverFunctions::userCreateGame);

        //List Games
        Spark.get("/game", serverFunctions::userListGames);
        //JOINING GAME
        Spark.put("/game", serverFunctions::userJoinGame);
        //Clearing it all
        Spark.delete("/db", serverFunctions::userClearDb);
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