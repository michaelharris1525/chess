package server;

import com.google.gson.Gson;
import dataaccess.UserDataAcess;
import dataaccess.UserMemorydao;
import model.AuthData;
import model.ErrorData;
import model.UserAlreadyExistsException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import spark.*;

import java.util.UUID;

public class Server {
    private static final UserMemorydao userMemorydao = new UserMemorydao();
    private static final Service serviceobj = new Service();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //Service serviceobj = new Service();

        Spark.post("/user", (Request req, Response res) -> {
            Gson serializer = new Gson();
            try {
                // Deserialize the request to UserData object
                UserData userObj = serializer.fromJson(req.body(), UserData.class);
                UserDataAcess userDataobj = new UserMemorydao();

                // Validate user input (e.g., check username and password and gmail are not blank
                if (userObj.username() == null || userObj.password() == null || userObj.email() == null) {
                    res.status(400); // Bad request
                    return serializer.toJson(new ErrorData("Error: bad request"));
                }
                //Validate if the user is in the database or not
                //is this register(RegisterRequest) Handler side?

                // Call service layer to register the user
                AuthData authData = serviceobj.register(userObj, userDataobj);


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
