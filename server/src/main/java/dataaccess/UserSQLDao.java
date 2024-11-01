package dataaccess;

import model.UnabletoConfigureDatabase;
import model.UserData;
import model.UserNameIsNullinMemoryDao;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserSQLDao implements UserDataAcess {

    @Override
    public UserData getuserdata(String username) {
        String query = "SELECT username, hashedPassword FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String retrievedUsername = rs.getString("username");
                String hashedPassword = rs.getString("hashedPassword");
                String gMail = rs.getString("gMail");
                return new UserData(retrievedUsername, hashedPassword, gMail);
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void adduserdata(UserData user) {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.username());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.email());
            stmt.executeUpdate();
        }
        catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getusersname(String username) throws UserNameIsNullinMemoryDao {
        String query = "SELECT username FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            } else {
                throw new UserNameIsNullinMemoryDao("Username not found in database.");
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getuserpassword(String username) {
        String query = "SELECT hashedPassword FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("hashedPassword");
            }
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        String query = "SELECT COUNT(*) FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    private final String[] createTables =  {
        """
            CREATE TABLE IF NOT EXISTS users (
                'username' VARCHAR(255) PRIMARY KEY,
                'password' VARCHAR(255) NOT NULL,
                'email' VARCHAR(255) UNIQUE NOT NULL
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






    /*
    public static void addUser(UserData user) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.username());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.email());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error adding user: " + e.getMessage());
        }
    }


    public static UserData getUserByUsername(String username) throws DataAccessException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserData(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user: " + e.getMessage());
        }
        return null;
    }
     */

