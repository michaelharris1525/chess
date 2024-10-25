package dataaccess;

import model.AuthData;
//import model.UserData;

import java.util.HashMap;

public class AuthTokenStorage implements AuthTokenDataAcess{
    //string authtoken and string username
    final private HashMap<String, AuthData> authDatas = new HashMap<>();

    public void addAuthToken(AuthData token) {
        //UserData user = new UserData(username)
        authDatas.put(token.authToken(), token);
    }
    public void clearuserdatabase(){
        authDatas.clear();
    }
    public void deleteauthtoken(String authtoken){
        authDatas.remove(authtoken);
    }
    public AuthData getauthtoken(AuthData author) {
        //UserData user = new UserData(username)
        AuthData returndata = authDatas.get(author.authToken());
        return returndata;
    }
    public AuthData getauthtoken(String authToken) {
        //UserData user = new UserData(username)
        AuthData returndata = authDatas.get(authToken);
        return returndata;
    }
}
