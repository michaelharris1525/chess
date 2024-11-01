package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public DataAccessException(String message) {
        super(message);
    }
    // New constructor with a message and cause
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
