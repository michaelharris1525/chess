package model;

public class UserPasswordIsWrong extends Exception {
    public UserPasswordIsWrong(String message) {
        super(message);
    }
}

