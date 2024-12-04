package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import spark.*;
import dataaccess.UserSQLDao;

import server.websocket.WebSocketHandler;

public class Server {
    //SQL Databases
    private final UserSQLDao userSQL = new UserSQLDao();
    private final GameSQLDao gameSQLDAO = new GameSQLDao();
    private final AuthSQLTokenClass authSQL = new AuthSQLTokenClass();

    //Service Helper
    private final ServerHelper serverFunctions = new ServerHelper();
    //websockets
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();

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
        //Websocket
        Spark.webSocket("/ws", webSocketHandler);
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