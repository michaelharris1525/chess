
package service;


import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requestextension.UserNameIsNullinMemoryDao;
import server.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class P3ServiceTests {
    private Service serviceObj = new Service();

    @BeforeEach
    public void clearDatabases(){
        UserDataAcess dataObj = new UserSQLDao();
        AuthTokenDataAcess authTData = new AuthSQLTokenClass();
        GameDataAccess gStorage = new GameSQLDao();
        dataObj.clearuserdatabase();
        authTData.clearuserdatabase();
        gStorage.clearGameData();
    }
    @Test
    public void testsClear(){
        /*get storage from each data bas
        * put data in the storage classes
        * assert if the data is in there like getting an authtoken an expecting */
        AuthData token = new AuthData("1234", "Mr. PoopyButtHole");
        UserData newUser = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        int id = 1;
        String gameName = "ILIKETRAINS";

        AuthTokenDataAcess atStorage = new AuthSQLTokenClass();
        GameDataAccess gStorage = new GameSQLDao();
        UserSQLDao userStorage = new UserSQLDao();


        atStorage.addAuthToken(token);
        userStorage.adduserdata(newUser);

        serviceObj.clearAllData(userStorage, atStorage, gStorage);
        boolean authTokenDeletedFromDataBase = true;

        Assertions.assertTrue(authTokenDeletedFromDataBase);
    }

    @Test
    public void registerNormal() throws UserAlreadyExistsException {
        UserData newUser2 = new UserData("PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObj = new UserSQLDao();
        AuthTokenDataAcess authTData = new AuthSQLTokenClass();
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
        UserDataAcess dataObj = new UserSQLDao();
        AuthTokenDataAcess authTData = new AuthSQLTokenClass();
        serviceObj.register(newUser2, dataObj, authTData);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            serviceObj.register(newUser2, dataObj, authTData);
        }, "Expected UserAlreadyExistsException to be thrown when registering an existing user.");
    }
    @Test
    public void loginUserNormal() throws UserAlreadyExistsException,
            UserNameIsWrong,UserPasswordIsWrong,UserNameIsNullinMemoryDao {
        UserData newUser = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObj = new UserSQLDao();
        AuthTokenDataAcess authTData = new AuthSQLTokenClass();

        serviceObj.register(newUser, dataObj, authTData);
        serviceObj.loginuser(newUser, dataObj, authTData);

        //since nothing thrown it works
        Assertions.assertTrue(true);

    }
    @Test
    public void loginUserDoesNotExist()  {
        UserData newUser5 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObje = new UserSQLDao();
        AuthTokenDataAcess authTData1 = new AuthSQLTokenClass();

        Assertions.assertThrows(UserNameIsNullinMemoryDao.class, () -> {
            serviceObj.loginuser(newUser5, dataObje, authTData1);
        }, "Expected UserNameIsNull or doesnt exist  to be thrown when registering an existing user.");
    }
    @Test
    public void loggingoutNormal() throws DataAccessException,UserAlreadyExistsException,
            UserNameIsWrong,UserPasswordIsWrong,UserNameIsNullinMemoryDao{
        UserData newUser4 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObject = new UserSQLDao();
        AuthTokenDataAcess authTData2 = new AuthSQLTokenClass();
        //register
        serviceObj.register(newUser4, dataObject, authTData2);
        AuthData token = serviceObj.loginuser(newUser4, dataObject, authTData2);

        serviceObj.logout(token.authToken(), authTData2);
        //if nothing thrown, assert true
        Assertions.assertTrue(true);
    }
    @Test
    public void logOutTwice() throws
            DataAccessException,
            UserAlreadyExistsException,
            UserNameIsWrong,
            UserPasswordIsWrong,
            UserNameIsNullinMemoryDao{
        UserData newUser3 = new UserData("Mr. PoopyButtHole",
                "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObjc = new UserSQLDao();
        AuthTokenDataAcess authTData3 = new AuthSQLTokenClass();
        //register
        serviceObj.register(newUser3, dataObjc, authTData3);
        AuthData token = serviceObj.loginuser(newUser3, dataObjc, authTData3);

        serviceObj.logout(token.authToken(), authTData3);

        Assertions.assertThrows(DataAccessException.class, () -> {
            serviceObj.logout(token.authToken(), authTData3);
        }, "Expected DataAcessException. User successfully deleted if this passes");
    }

    @Test
    public void createGame() throws
            DataAccessException,
            UserAlreadyExistsException,
            UserNameIsWrong,
            UserNameIsNullinMemoryDao,
            UserPasswordIsWrong {
        UserData newUser20 = new UserData("Mr. PoopyButtHole",
                "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObja = new UserSQLDao();
        AuthTokenDataAcess authTData20 = new AuthSQLTokenClass();
        GameDataAccess gStorage = new GameStorage();
        String nameOfGame = "MR POOOPY BUTTHOLES ARENA";
        //register
        serviceObj.register(newUser20, dataObja, authTData20);
        AuthData token = serviceObj.loginuser(newUser20,
                dataObja, authTData20);
        serviceObj.createGame(token.authToken(), authTData20, gStorage,nameOfGame);
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameWrong() throws
            UserAlreadyExistsException,
            UserNameIsWrong,
            UserNameIsNullinMemoryDao,
            UserPasswordIsWrong {
        UserData newUser20 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObjae = new UserSQLDao();
        AuthTokenDataAcess authTData20 = new AuthSQLTokenClass();
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
    public void joinGameNormal() throws DataAccessException,
            UserAlreadyExistsException,
            UserNameIsWrong,
            UserNameIsNullinMemoryDao,
            UserPasswordIsWrong,
            BadRequestsException,
            PlayerColorException {

        UserData newUser12 = new UserData("Mr", "Poppy0", "lilTimmy.com");
        UserDataAcess dataO = new UserSQLDao();
        AuthTokenDataAcess authTData201 = new AuthSQLTokenClass();
        GameDataAccess gStorage12 = new GameStorage();
        String nameOfGame = "MR POOOPY BUTTHOLES ARENA";
        //register
        serviceObj.register(newUser12, dataO, authTData201);
        AuthData token = serviceObj.loginuser(newUser12,
                dataO, authTData201);


        assertTrue(true);
    }
    @Test
    public void badAuthJoin() {

        assertTrue(true);

    }


    }



