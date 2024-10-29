package dataaccess;

import model.UserData;
import model.UserNameIsNullinMemoryDao;

import java.util.HashMap;

public class UserMemorydao implements UserDataAcess {
    //handles CRUD( CREATE, READ, UPDATE, DELETE
    //make multiple daos for each object of data
    final private HashMap<String, UserData> totalUsers = new HashMap<>();

    @Override
    public UserData getuserdata(String username) {
        //UserData user = new UserData(username)
        UserData usersss= totalUsers.get(username);
        return usersss;
    }

    public String getuserpassword(String username) {
        //UserData user = new UserData(username)
        UserData usersss= totalUsers.get(username);
        return usersss.password();
    }

    public String getusersname(String username) throws UserNameIsNullinMemoryDao {
        //UserData user = new UserData(username)
        UserData usersss= totalUsers.get(username);
        if(usersss == null){
            throw new UserNameIsNullinMemoryDao("Messes up in mem dao");
        }
        return usersss.username();
    }

    public void adduserdata(UserData user) {
        //UserData user = new UserData(username)
        totalUsers.put(user.username(), user);
    }
    public void clearuserdatabase(){
        totalUsers.clear();
    }
    public boolean isEmpty() {
        if(totalUsers.isEmpty()){
            return true;
        }
        return false;
    }
}
