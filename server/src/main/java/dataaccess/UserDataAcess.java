package dataaccess;

import model.UserData;

public interface UserDataAcess {
    UserData getuserdata(String username);
    void adduserdata(UserData user);

}
