package dataaccess;

import model.UserData;
import model.UserNameIsNullinMemoryDao;

public interface UserDataAcess {
    UserData getuserdata(String username);
    void adduserdata(UserData user);
    String getusersname(String username) throws UserNameIsNullinMemoryDao;
    String getuserpassword(String username);
    boolean isEmpty();
}
