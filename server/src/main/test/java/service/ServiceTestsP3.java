package service;


import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserAlreadyExistsException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.Service;


public class ServiceTestsP3 {
    private Service serviceObj = new Service();
    @Test
    public void testsClear(){
        /*get storage from each data base
        * put data in the storage classes
        * assert if the data is in there like getting an authtoken an expecting */
        AuthData token = new AuthData("1234", "Mr. PoopyButtHole");
        UserData newUser = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        int id = 1;
        String gameName = "ILIKETRAINS";

        AuthTokenDataAcess ATStorage = new AuthTokenStorage();
        GameDataAccess GStorage = new GameStorage();
        UserMemorydao UserStorage = new UserMemorydao();


        ATStorage.addAuthToken(token);
        GStorage.addNewGame(id, gameName);
        UserStorage.adduserdata(newUser);

        serviceObj.clearAllData(UserStorage, ATStorage, GStorage);
        boolean authTokenDeletedFromDataBase = false;

        if(UserStorage.isEmpty() &&
        GStorage.isEmpty() == true &&  !ATStorage.containsAuthToken("1234")){
            authTokenDeletedFromDataBase = true;
        }

        Assertions.assertTrue(authTokenDeletedFromDataBase);
    }

    @Test
    public void ResisterNormal() throws UserAlreadyExistsException {
        //UserData newUser = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserData newUser2 = new UserData("PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObj = new UserMemorydao();
        AuthTokenDataAcess authTData = new AuthTokenStorage();
        //dataobj.adduserdata(newUser);
        boolean passorfail = false;
        serviceObj.register(newUser2, dataObj, authTData);
        if(!dataObj.isEmpty()){
            passorfail = true;
        }

        Assertions.assertTrue(passorfail);
    }
    @Test
    public void UserNameIsAlreadyInThere() throws UserAlreadyExistsException {
        UserData newUser = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserData newUser2 = new UserData("Mr. PoopyButtHole", "Poppy0",  "JustBasicJosephatgmail.com");
        UserDataAcess dataObj = new UserMemorydao();
        AuthTokenDataAcess authTData = new AuthTokenStorage();
        //dataobj.adduserdata(newUser);
        boolean passorfail = false;
        serviceObj.register(newUser2, dataObj, authTData);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            serviceObj.register(newUser2, dataObj, authTData);
        }, "Expected UserAlreadyExistsException to be thrown when registering an existing user.");
    }

    }


