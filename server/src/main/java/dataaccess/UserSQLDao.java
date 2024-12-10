package dataaccess;

import model.UnabletoConfigureDatabase;
import model.UserData;
import requestextension.UserNameIsNullinMemoryDao;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserSQLDao implements UserDataAcess {

    @Override
    public void clearuserdatabase() {
        String sql = "DELETE FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserData getuserdata(String username) {
        String query = "SELECT username, password, email FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String retrievedUsername = rs.getString("username");
                String hashedPassword = rs.getString("password");
                String gMail = rs.getString("email");
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
        //this column does not exist
        String query = "SELECT Password FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("password");
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
                `username` VARCHAR(255) PRIMARY KEY,
                `password` VARCHAR(255) NOT NULL,
                `email` VARCHAR(255) UNIQUE NOT NULL
            );
            """};



    public void userconfigureDatabase() throws UnabletoConfigureDatabase, DataAccessException {
        DatabaseManager.configureDatabase(createTables);
    }
}