package dataaccess;

import model.AuthData;

public interface AuthTokenDataAcess {
    void addAuthToken(AuthData token);
    void clearuserdatabase();
    AuthData getauthtoken(String author);
    void deleteauthtoken(String authtoken);
}
