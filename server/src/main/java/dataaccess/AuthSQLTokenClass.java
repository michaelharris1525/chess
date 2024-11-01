package dataaccess;

import model.AuthData;
import model.UnabletoConfigureDatabase;

import java.sql.SQLException;

public class AuthSQLTokenClass implements AuthTokenDataAcess{
    @Override
    public void addAuthToken(AuthData token) {

    }

    @Override
    public void clearuserdatabase() {

    }

    @Override
    public AuthData getauthtoken(String author) {
        return null;
    }

    @Override
    public void deleteauthtoken(String authtoken) {

    }

    @Override
    public boolean containsAuthToken(String authToken) {
        return false;
    }

    //String authToken, String username
    private final String[] createTables =  {
            """
            CREATE TABLE IF NOT EXISTS users (
                'authToken' VARCHAR(255),
                'userName' VARCHAR(255),
            );
            """};



    public void configureDatabase() throws UnabletoConfigureDatabase, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createTables) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new UnabletoConfigureDatabase(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}