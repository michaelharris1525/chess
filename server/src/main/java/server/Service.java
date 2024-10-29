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
                    GameDataAccess gameStoraged, String nameOFGame) throws DataAccessException {
        //validate AuthToken, check if auth data is null or not
        if(authdataac.getauthtoken(auth) == null){
            throw new DataAccessException("you screwed up");
        }
        //if validated create new game
        int new_gameID = gameStoraged.getSize() + 1;
        gameStoraged.addNewGame(new_gameID, nameOFGame);

        //also need to check if there is a new name given into it

        return new_gameID;
    }
    void joinGame(String authToken, AuthTokenDataAcess authdataac,
                  GameDataAccess gameStoraged, GameData gameDATAFROMREQUEST, JoinGameRequest joinRequest) throws DataAccessException, PlayerColorException, BadRequestsException {
        if(authdataac.getauthtoken(authToken) == null){
            throw new DataAccessException("you screwed up");
        }

        if (gameDATAFROMREQUEST == null) {
            throw new BadRequestsException("GAMEDATAFROMREQUEST");
        }

        String playerColor = joinRequest.getPlayerColor();
        //grab authdata to get username
        AuthData authdataforusername = authdataac.getauthtoken(authToken);

        if (("WHITE".equals(playerColor) && gameDATAFROMREQUEST.getWhiteColor() != null) ||
                ("BLACK".equals(playerColor) && gameDATAFROMREQUEST.getBlackColor() != null)) {
            throw new PlayerColorException("Problem with white and player color, already taken");
        }
        if ("WHITE".equals(playerColor)) {
            joinRequest.updatePlayerWhite();
            gameStoraged.UpdateWhiteColor(gameDATAFROMREQUEST.gameID(),authdataforusername.username());// set WHITE player
        }
        else if ("BLACK".equals(playerColor)) {
            joinRequest.updatePlayerBlack();
            gameStoraged.UpdateBlackColor(gameDATAFROMREQUEST.gameID(),authdataforusername.username()); // set BLACK player
        }
        //need to pass in the username, you get that from the authdata you receive when you check if the game exists or not by the gameID



        //also need to update color as well the username


    }

    // CLEAR, make 11 unit tests
    public void clearAllData(UserMemorydao userd, AuthTokenDataAcess authdata, GameDataAccess gameData){
        userd.clearuserdatabase();
        authdata.clearuserdatabase();
        gameData.clearGameData();
    }

}


