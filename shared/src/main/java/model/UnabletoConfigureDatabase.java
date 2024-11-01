package model;

public class UnabletoConfigureDatabase extends Exception {
    final private int statuscode;
    public UnabletoConfigureDatabase(int statusCode, String message) {
        super(message);
        this.statuscode = statusCode;

    }
}

