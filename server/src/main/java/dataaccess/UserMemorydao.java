package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserMemorydao implements UserDataAcess {
    //handles CRUD( CREATE, READ, UPDATE, DELETE
    //make multiple daos for each object of data
    final private static HashMap<String, UserData> totalUsers = new HashMap<>();

    @Override
    public UserData getuserdata(String username) {
        //UserData user = new UserData(username)
        UserData usersss= totalUsers.get(username);
        return usersss;
    }

    public void adduserdata(UserData user) {
        //UserData user = new UserData(username)
        totalUsers.put(user.username(), user);
    }
}
