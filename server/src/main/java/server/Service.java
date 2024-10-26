package server;

import dataaccess.*;
import model.*;

import java.util.UUID;

public class Service {
    //REGISTER
    AuthData register(UserData user, UserDataAcess dataobj, AuthTokenDataAcess authDataAcessobj) throws UserAlreadyExistsException {
        //on your handler do some checking in handler on server for registering
        AuthData authdata = new AuthData(UUID.randomUUID().toString(), user.username());

        //check if the username is in the database or not, should return null if its not in there
        if(dataobj.getuserdata(user.username()) != null){
            //if it doesn't work throw an exception
            throw new UserAlreadyExistsException("User already exists: ");
        }
        //add to database if there is no username, and auth data needs to
        dataobj.adduserdata(user);

        //create auth data token and add it to database
        authDataAcessobj.addAuthToken(authdata);

        return authdata;
    }

    //LOGIN
    //if you have a problem, change useralreadyexistsexcpetion by adding a new exception super class
    AuthData loginuser(UserData user, UserDataAcess dataobj,AuthTokenDataAcess authDataAcessobj) throws UserNameIsWrong, UserPasswordIsWrong, UserNameIsNullinMemoryDao {
        //on your handler do some checking in handler on server for registering
        AuthData authdata = new AuthData(UUID.randomUUID().toString(), user.username());

        if(!dataobj.getusersname(user.username()).equals(user.username())) {
            //if user doesn't match throw an exception
            throw new UserNameIsWrong("Error: unauthorized");
        }
        if(!dataobj.getuserpassword(user.username()).equals(user.password())){
            //if password does not match throw exception
            throw new UserPasswordIsWrong("Error: unauthorized");
        }
        //create auth data token and add it to database
        authDataAcessobj.addAuthToken(authdata);
        return authdata;
    }

    //LOGOUT
    void logout(String authtoken, AuthTokenDataAcess authdataac) throws DataAccessException {
        if(authdataac.getauthtoken(authtoken) == null){
            throw new DataAccessException("Messed up");
        }

        authdataac.deleteauthtoken(authtoken);
    }
    int creategame(String auth, AuthTokenDataAcess authdataac,
                    GameDataAccess gameStoraged) throws DataAccessException {
        //validate AuthToken, check if auth data is null or not
        if(authdataac.getauthtoken(auth) == null){
            throw new DataAccessException("you screwed up");
        }
        //if validated create new game
        int new_gameID = gameStoraged.getSize() + 1;
        gameStoraged.addNewGame(new_gameID);

        return new_gameID;
    }
    void joinGame(String authToken, AuthTokenDataAcess authdataac,
                  GameDataAccess gameStoraged) throws DataAccessException {
        if(authdataac.getauthtoken(authToken) == null){
            throw new DataAccessException("you screwed up");
        }

        //does game exists
        if(!gameStoraged.isGameReal())

    }

    // CLEAR
    void clearAllData(UserMemorydao userd, AuthTokenDataAcess authdata, GameDataAccess gameData){
        userd.clearuserdatabase();
        authdata.clearuserdatabase();
        gameData.clearGameData();
    }

}


