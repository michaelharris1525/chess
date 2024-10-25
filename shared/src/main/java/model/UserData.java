package model;

import java.util.HashMap;
import java.util.Map;

public record UserData(String username, String password, String email) {
//    public Map<String, String> toLoginInfo() {
//        Map<String, String> loginInfo = new HashMap<>();
//        loginInfo.put("username", this.username);
//        loginInfo.put("password", this.password);
//        return loginInfo;
//    }
}
