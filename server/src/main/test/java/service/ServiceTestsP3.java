package service;


import com.sun.source.tree.AssertTree;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ServiceTestsP3 {
    private Service serviceObj = new Service();

    void badCreateUserAndLoginAndJoinGameWhite(UserData newUser20, UserDataAcess dataObja, AuthTokenDataAcess authTData201,
                                       GameDataAccess gStorage, String nameOfGame) throws UserNameIsWrong, UserNameIsNullinMemoryDao, UserPasswordIsWrong, UserAlreadyExistsException, BadRequestsException, DataAccessException, PlayerColorException {
        //register
        serviceObj.register(newUser20, dataObja, authTData201);
        AuthData token = serviceObj.loginuser(newUser20,
                dataObja, authTData201);
        int gameId= serviceObj.createGame(token.authToken(), authTData201, gStorage, nameOfGame);

        // Retrieve the created GameData from the storage for the game ID
        GameData gameDataFromRequest = gStorage.getGameData(gameId);

        // Create a JoinGameRequest object with required fields
        JoinGameRequest joinRequest = new JoinGameRequest();
        joinRequest.changeIdForTesting(gameId);
        joinRequest.updatePlayerWhite();

        serviceObj.joinGame(null, authTData201, gStorage, gameDataFromRequest,joinRequest);

    }
    @Test
    public void testsClear(){
        /*get storage from each data base
        * put data in the storage classes
        * assert if the data is in there like getting an authtoken an expecting */
        AuthData token = new AuthData("1234", "Mr. PoopyButtHole");
        UserData newUser = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        int id = 1;
        String gameName = "ILIKETRAINS";

        AuthTokenDataAcess atStorage = new AuthTokenStorage();
        GameDataAccess gStorage = new GameStorage();
        UserMemorydao userStorage = new UserMemorydao();


        atStorage.addAuthToken(token);
        gStorage.addNewGame(id, gameName);
        userStorage.adduserdata(newUser);

        serviceObj.clearAllData(userStorage, atStorage, gStorage);
        boolean authTokenDeletedFromDataBase = false;

        if(userStorage.isEmpty() &&
        gStorage.isEmpty() == true &&  !atStorage.containsAuthToken("1234")){
            authTokenDeletedFromDataBase = true;
        }

        Assertions.assertTrue(authTokenDeletedFromDataBase);
    }

    @Test
    public void registerNormal() throws UserAlreadyExistsException {
        UserData newUser2 = new UserData("PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObj = new UserMemorydao();
        AuthTokenDataAcess authTData = new AuthTokenStorage();
        boolean passorfail = false;
        serviceObj.register(newUser2, dataObj, authTData);
        if(!dataObj.isEmpty()){
            passorfail = true;
        }

        Assertions.assertTrue(passorfail);
    }
    @Test
    public void userNameIsAlreadyInThere() throws UserAlreadyExistsException {
        UserData newUser2 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObj = new UserMemorydao();
        AuthTokenDataAcess authTData = new AuthTokenStorage();
        serviceObj.register(newUser2, dataObj, authTData);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            serviceObj.register(newUser2, dataObj, authTData);
        }, "Expected UserAlreadyExistsException to be thrown when registering an existing user.");
    }
    @Test
    public void loginUserNormal() throws UserAlreadyExistsException, UserNameIsWrong,UserPasswordIsWrong,UserNameIsNullinMemoryDao {
        UserData newUser = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObj = new UserMemorydao();
        AuthTokenDataAcess authTData = new AuthTokenStorage();
        //register
        serviceObj.register(newUser, dataObj, authTData);
        serviceObj.loginuser(newUser, dataObj, authTData);

        //since nothing thrown it works
        Assertions.assertTrue(true);

    }
    @Test
    public void loginUserDoesNotExist() throws UserAlreadyExistsException, UserNameIsWrong,UserPasswordIsWrong,UserNameIsNullinMemoryDao {
        UserData newUser5 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObje = new UserMemorydao();
        AuthTokenDataAcess authTData1 = new AuthTokenStorage();

        Assertions.assertThrows(UserNameIsNullinMemoryDao.class, () -> {
            serviceObj.loginuser(newUser5, dataObje, authTData1);
        }, "Expected UserNameIsNull or doesnt exist  to be thrown when registering an existing user.");
    }
    @Test
    public void loggingoutNormal() throws DataAccessException,UserAlreadyExistsException, UserNameIsWrong,UserPasswordIsWrong,UserNameIsNullinMemoryDao{
        UserData newUser4 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObject = new UserMemorydao();
        AuthTokenDataAcess authTData2 = new AuthTokenStorage();
        //register
        serviceObj.register(newUser4, dataObject, authTData2);
        AuthData token = serviceObj.loginuser(newUser4, dataObject, authTData2);

        serviceObj.logout(token.authToken(), authTData2);
        //if nothing thrown, assert true
        Assertions.assertTrue(true);
    }
    @Test
    public void logOutTwice() throws DataAccessException,UserAlreadyExistsException, UserNameIsWrong,UserPasswordIsWrong,UserNameIsNullinMemoryDao{
        UserData newUser3 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObjc = new UserMemorydao();
        AuthTokenDataAcess authTData3 = new AuthTokenStorage();
        //register
        serviceObj.register(newUser3, dataObjc, authTData3);
        AuthData token = serviceObj.loginuser(newUser3, dataObjc, authTData3);

        serviceObj.logout(token.authToken(), authTData3);

        Assertions.assertThrows(DataAccessException.class, () -> {
            serviceObj.logout(token.authToken(), authTData3);
        }, "Expected DataAcessException. User successfully deleted if this passes");
    }

    @Test
    public void createGame() throws DataAccessException, UserAlreadyExistsException, UserNameIsWrong, UserNameIsNullinMemoryDao, UserPasswordIsWrong {
        UserData newUser20 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObja = new UserMemorydao();
        AuthTokenDataAcess authTData20 = new AuthTokenStorage();
        GameDataAccess gStorage = new GameStorage();
        String nameOfGame = "MR POOOPY BUTTHOLES ARENA";
        //register
        serviceObj.register(newUser20, dataObja, authTData20);
        AuthData token = serviceObj.loginuser(newUser20,
                dataObja, authTData20);
        serviceObj.createGame(token.authToken(), authTData20, gStorage,nameOfGame);
        Assertions.assertTrue(!gStorage.isEmpty());
    }

    @Test
    public void createGameWrong() throws DataAccessException, UserAlreadyExistsException, UserNameIsWrong, UserNameIsNullinMemoryDao, UserPasswordIsWrong {
        UserData newUser20 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObjae = new UserMemorydao();
        AuthTokenDataAcess authTData20 = new AuthTokenStorage();
        GameDataAccess gStorage = new GameStorage();
        String nameOfGame = "MR POOOPY BUTTHOLES ARENA";
        //register
        serviceObj.register(newUser20, dataObjae, authTData20);
        serviceObj.loginuser(newUser20,
                dataObjae, authTData20);
        String wrongToken = "False Token";
        Assertions.assertThrows(DataAccessException.class, () -> {
            serviceObj.createGame(wrongToken, authTData20, gStorage,nameOfGame);
        }, "Expected DataAcessException. User successfully deleted if this passes");

    }
    @Test
    public void joinGameNormal() throws DataAccessException, UserAlreadyExistsException, UserNameIsWrong, UserNameIsNullinMemoryDao, UserPasswordIsWrong, BadRequestsException, PlayerColorException {

        UserData newUser12 = new UserData("Mr", "Poppy0", "lilTimmy.com");
        UserDataAcess dataO = new UserMemorydao();
        AuthTokenDataAcess authTData201 = new AuthTokenStorage();
        GameDataAccess gStorage12 = new GameStorage();
        String nameOfGame = "MR POOOPY BUTTHOLES ARENA";
        //register
        serviceObj.register(newUser12, dataO, authTData201);
        AuthData token = serviceObj.loginuser(newUser12,
                dataO, authTData201);
        int gameId= serviceObj.createGame(token.authToken(), authTData201, gStorage12, nameOfGame);

        // Retrieve the created GameData from the storage for the game ID
        GameData gameDataFromRequest = gStorage12.getGameData(gameId);

        // Create a JoinGameRequest object with required fields
        JoinGameRequest joinRequest = new JoinGameRequest();
        joinRequest.changeIdForTesting(gameId);
        joinRequest.updatePlayerWhite();

        serviceObj.joinGame(token.authToken(), authTData201, gStorage12, gameDataFromRequest,joinRequest);
        assertEquals(nameOfGame, gameDataFromRequest.gameName());
    }
    @Test
    public void badAuthJoin() throws DataAccessException, UserAlreadyExistsException, UserNameIsWrong, UserNameIsNullinMemoryDao, UserPasswordIsWrong, BadRequestsException, PlayerColorException {
        UserData newUser2002 = new UserData("Mr. PoopyButtHole", "Poppy0", "JustBasicJosephatgmail.com");
        UserData newUser1902 = new UserData("beans", "Poppy0", "JustBasicJosephatgmail.com");

        UserDataAcess dataObja = new UserMemorydao();
        AuthTokenDataAcess authTData201 = new AuthTokenStorage();
        GameDataAccess gStorage = new GameStorage();
        String nameOfGame = "MR POOOPY BUTTHOLES ARENA";

        Assertions.assertThrows(DataAccessException.class, () -> {
            badCreateUserAndLoginAndJoinGameWhite(newUser2002,dataObja,authTData201,gStorage,nameOfGame);
            }, "Expected DataAccessException, Using the");

    }


    }


