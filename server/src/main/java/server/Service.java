package server;

import dataaccess.UserDataAcess;
import model.AuthData;
import model.UserAlreadyExistsException;
import model.UserData;

import java.util.UUID;

public class Service {
    AuthData register(UserData user, UserDataAcess dataobj) throws UserAlreadyExistsException {
        //on your handler do some checking in handler on server for registering
        AuthData authdata = new AuthData(UUID.randomUUID().toString(), user.username());

        //check if the username is in the database or not, should return null if its not in there
        if(dataobj.getuserdata(user.username()) != null){
            //if it doesn't work throw an exception
            throw new UserAlreadyExistsException("User already exists: " + user.username());
        }
        //add to database if there is no username, and auth data needs to
        dataobj.adduserdata(user);
        return authdata;
    }

}


