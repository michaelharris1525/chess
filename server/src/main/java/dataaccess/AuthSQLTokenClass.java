package dataaccess;

import model.AuthData;
import model.UnabletoConfigureDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSQLTokenClass implements AuthTokenDataAcess{
    // Add a new auth token to the database
    @Override
    public void addAuthToken(AuthData token) {
        String sql = "INSERT INTO authTokens (authToken, userName) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token.authToken());
            stmt.setString(2, token.username());
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    // Clear all auth tokens from the database
    @Override
    public void clearuserdatabase() {
        String sql = "DELETE FROM authTokens";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    // Retrieve an auth token by username
    @Override
    public AuthData getauthtoken(String token) {
        String query = "SELECT authToken, userName FROM authTokens WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String userName = rs.getString("userName");
                return new AuthData(token, userName);
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Delete an auth token from the database
    @Override
    public void deleteauthtoken(String authToken) {
        String sql = "DELETE FROM authTokens WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    // Check if an auth token exists in the database
    @Override
    public boolean containsAuthToken(String authToken) {
        String query = "SELECT COUNT(*) FROM authTokens WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, authToken);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
//    @Override
//    public void addAuthToken(AuthData token) {
//
//    }
//
//    @Override
//    public void clearuserdatabase() {
//
//    }
//
//    @Override
//    public AuthData getauthtoken(String author) {
//        return null;
//    }
//
//    @Override
//    public void deleteauthtoken(String authtoken) {
//
//    }
//
//    @Override
//    public boolean containsAuthToken(String authToken) {
//        return false;
//    }

    //String authToken, String username
    private final String[] createTables =  {
            """
            CREATE TABLE IF NOT EXISTS authTokens (
                `authToken` VARCHAR(255) PRIMARY KEY,
                `userName` VARCHAR(255)
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