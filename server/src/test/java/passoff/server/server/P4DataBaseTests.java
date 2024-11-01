package passoff.server.server;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Collection;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;

import dataaccess.DataAccessException;
import dataaccess.GameSQLDao;
import org.junit.jupiter.api.*;
import server.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class P4DataBaseTests {
    private GameSQLDao gameSQLDao;
    private Connection connection;
    private Service serviceObj;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        // Initialize your database setup here, if needed
        // For example, create an in-memory database
        // You can also create tables if necessary
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        connection = DatabaseManager.getConnection();
        gameSQLDao = new GameSQLDao(); // Pass the connection to your DAO
        DatabaseManager.createDatabase(); // Ensure the database exists
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close(); // Close the connection after each test
        }
    }

    @Test
    public void testAddAuthTokenFail() throws DataAccessException {
        String username = "testUser";
        String token = "sampleAuthToken";
        AuthData authD = new AuthData(token, username);

        AuthSQLTokenClass authSQLTokenClass = new AuthSQLTokenClass();
        authSQLTokenClass.clearuserdatabase(); // Clear previous tokens if necessary
        authSQLTokenClass.addAuthToken(authD); // Add new token
        AuthData retrievedToken = authSQLTokenClass.getauthtoken(username); // Retrieve the token

        assertNull(retrievedToken);
    }

    @Test
    void testAddNewGame() {
        // Arrange
        String gameName = "Test Game";
        int gameId = 1;

        // Act
        gameSQLDao.addNewGame(gameId, gameName);

        // Assert
        GameData gameData = gameSQLDao.getGameData(gameId);
        assertNotNull(gameData);
        assertEquals(gameId, gameData.getGameID());
        assertEquals(gameName, gameData.getGameName());
    }

    @Test
    void testClearGameData() {
        // Arrange
        gameSQLDao.addNewGame(1, "Game 1");
        gameSQLDao.addNewGame(2, "Game 2");

        // Act
        gameSQLDao.clearGameData();

        // Assert
        assertTrue(gameSQLDao.isEmpty());
    }

    @Test
    void testGetGameData() {
        // Arrange
        int gameId = 1;
        String whiteUsername = "WhitePlayer";
        String blackUsername = "BlackPlayer";
        String gameName = "Test Game";

        gameSQLDao.addNewGame(gameId, gameName);
        gameSQLDao.updateWhiteColor(gameId, whiteUsername);
        gameSQLDao.updateBlackColor(gameId, blackUsername);

        // Act
        GameData gameData = gameSQLDao.getGameData(gameId);

        // Assert
        assertNotNull(gameData);
        assertEquals(gameId, gameData.getGameID());
        assertEquals(whiteUsername, gameData.getWhiteColor());
        assertEquals(blackUsername, gameData.getBlackColor());
        assertEquals(gameName, gameData.getGameName());
    }

    @Test
    void testGetAllGameData() {
        // Arrange
        gameSQLDao.addNewGame(1, "Game 1");
        gameSQLDao.addNewGame(2, "Game 2");

        // Act
        Collection<GameData> games = gameSQLDao.getAllGameData();

        // Assert
        assertEquals(2, games.size());
    }
    @Test
    public void testaddUserData() throws DataAccessException {
        UserData testUser = new UserData("newbuddy", "testPass", "test@example.com");
        UserSQLDao userSQLDao = new UserSQLDao();
        userSQLDao.clearuserdatabase();
        userSQLDao.adduserdata(testUser);
        UserData retrievedUser = userSQLDao.getuserdata("newbuddy");
        assertNotNull(retrievedUser);
        assertEquals("newbuddy", retrievedUser.username());
        assertEquals("test@example.com", retrievedUser.email());
        userSQLDao.clearuserdatabase();
    }
    @Test
    public void testaddUserDataFail() throws DataAccessException {
        UserData testUser = new UserData(null,null,null);
        UserSQLDao userSQLDao = new UserSQLDao();
        userSQLDao.clearuserdatabase();
        userSQLDao.adduserdata(testUser);
        UserData retrievedUser = userSQLDao.getuserdata(testUser.username());
        assertNull(retrievedUser);
        userSQLDao.clearuserdatabase();
    }
    @Test
    public void testGetUserDataSuccess() throws DataAccessException {
        UserData testUser = new UserData("testUser", "testPass", "test@example.com");
        UserSQLDao userSQLDao = new UserSQLDao();

        // Clear the database to ensure a clean state for the test
        userSQLDao.clearuserdatabase();

        // Add the test user to the database
        userSQLDao.adduserdata(testUser);

        // Retrieve the user data by username
        UserData retrievedUser = userSQLDao.getuserdata("testUser");

        // Verify that the retrieved user data matches the inserted data
        assertNotNull(retrievedUser, "Retrieved user should not be null");
        assertEquals(testUser.username(), retrievedUser.username(), "Username should match");
        assertEquals(testUser.email(), retrievedUser.email(), "Email should match");

        // Clean up after test
        userSQLDao.clearuserdatabase();
    }
    @Test
    public void testGetUserDataFail() throws DataAccessException {
        UserData testUser = new UserData(null,null,null);
        UserSQLDao userSQLDao = new UserSQLDao();

        // Clear the database to ensure a clean state for the test
        userSQLDao.clearuserdatabase();

        // Add the test user to the database
        userSQLDao.adduserdata(testUser);

        // Retrieve the user data by username
        UserData retrievedUser = userSQLDao.getuserdata(testUser.username());

        // Verify that the retrieved user data matches the inserted data
        assertNull(retrievedUser, "Retrieved user should not be null, nothing in UserData");

        // Clean up after test
        userSQLDao.clearuserdatabase();
    }
    @Test
    public void testGetUserPasswordSuccess() throws DataAccessException {
        UserData testUser = new UserData("testUser", "testPass", "test@example.com");
        UserSQLDao userSQLDao = new UserSQLDao();

        // Clear the database for a clean start
        userSQLDao.clearuserdatabase();

        // Add the test user to the database
        userSQLDao.adduserdata(testUser);

        // Retrieve the password by username
        String retrievedPassword = userSQLDao.getuserpassword("testUser");

        // Verify that the retrieved password matches the inserted password
        assertNotNull(retrievedPassword, "Retrieved password should not be null");

        // Clean up after test
        userSQLDao.clearuserdatabase();
    }
    @Test
    public void testGetUserPasswordFail() throws DataAccessException {
        UserData testUser = new UserData("testUser", "testPass", "test@example.com");
        UserSQLDao userSQLDao = new UserSQLDao();

        // Clear the database for a clean start
        userSQLDao.clearuserdatabase();

        // Add the test user to the database
        userSQLDao.adduserdata(testUser);

        // Retrieve the password by username
        String retrievedPassword = userSQLDao.getuserpassword("testUser");

        // Verify that the retrieved password matches the inserted password
        assertNotNull(retrievedPassword, "Retrieved password should not be null");
        assertNotEquals(testUser.password(), retrievedPassword);
        // Clean up after test
        userSQLDao.clearuserdatabase();
    }

}
