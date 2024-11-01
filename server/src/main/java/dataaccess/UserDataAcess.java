package dataaccess;

import model.UserData;
import model.UserNameIsNullinMemoryDao;

public interface UserDataAcess{
    void clearuserdatabase();
    UserData getuserdata(String username);
    void adduserdata(UserData user);
    String getusersname(String username) throws UserNameIsNullinMemoryDao;
    String getuserpassword(String username);
    boolean isEmpty();
}
