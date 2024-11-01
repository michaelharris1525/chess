package passoff.server.server;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UnabletoConfigureDatabase;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import model.GameData;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class P4DataBaseTests {
    private GameSQLDao gameSQLDao;
    private Connection connection;

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
    public void testAddAuthToken() throws DataAccessException {
        String username = "testUser";
        String token = "sampleAuthToken";
        AuthData authD = new AuthData(token, username);

        AuthSQLTokenClass authSQLTokenClass = new AuthSQLTokenClass();
        authSQLTokenClass.clearuserdatabase(); // Clear previous tokens if necessary
        authSQLTokenClass.addAuthToken(authD); // Add new token
        AuthData retrievedToken = authSQLTokenClass.getauthtoken(username); // Retrieve the token

        assertNotNull(retrievedToken);
        assertEquals(token, retrievedToken.authToken());

        authSQLTokenClass.clearuserdatabase(); // Clean up
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
}


//public class P4DataBaseTests {
//    private GameSQLDao gameSQLDao;
//    private Connection connection;
//    @Test
//    public void testAddAuthToken() throws DataAccessException {


//        String username = "testUser";
//        String token = "sampleAuthToken";
//        AuthData authD = new AuthData(token, username);
//
//        // Create an instance of AuthSQLTokenClass
//        AuthSQLTokenClass authSQLTokenClass = new AuthSQLTokenClass();
//
//        // Clear the auth token data if necessary
//        authSQLTokenClass.clearuserdatabase(); // Assume this method exists to clear tokens
//
//        // Act
//        authSQLTokenClass.addAuthToken(authD); // Assume this method exists to add the token
//
//        // Retrieve the added token data
//        AuthData retrievedToken = authSQLTokenClass.getauthtoken(username); // Assume this method exists
//
//        // Assert
//        assertNotNull(retrievedToken.authToken());
//        assertEquals(token, retrievedToken.authToken());
//
//        // Clean up if needed
//        authSQLTokenClass.clearuserdatabase(); // Clean up test data
//    }
//
//    @Test
//    void testAddNewGame() {
//        // Arrange
//        String gameName = "Test Game";
//        int gameId = 1;
//
//        // Act
//        gameSQLDao.addNewGame(gameId, gameName);
//
//        // Assert
//        GameData gameData = gameSQLDao.getGameData(gameId);
//        Assertions.assertNotNull(gameData);
//        Assertions.assertEquals(gameId, gameData.getGameID());
//        Assertions.assertEquals(gameName, gameData.getGameName());
//    }
//
//    @Test
//    void testClearGameData() {
//        // Arrange
//        gameSQLDao.addNewGame(1, "Game 1");
//        gameSQLDao.addNewGame(2, "Game 2");
//
//        // Act
//        gameSQLDao.clearGameData();
//
//        // Assert
//        Assertions.assertTrue(gameSQLDao.isEmpty());
//    }
//
//    @Test
//    void testGetGameData() {
//        // Arrange
//        int gameId = 1;
//        String whiteUsername = "WhitePlayer";
//        String blackUsername = "BlackPlayer";
//        String gameName = "Test Game";
//
//        gameSQLDao.addNewGame(gameId, gameName);
//        gameSQLDao.updateWhiteColor(gameId, whiteUsername);
//        gameSQLDao.updateBlackColor(gameId, blackUsername);
//
//        // Act
//        GameData gameData = gameSQLDao.getGameData(gameId);
//
//        // Assert
//        Assertions.assertNotNull(gameData);
//        Assertions.assertEquals(gameId, gameData.getGameID());
//        Assertions.assertEquals(whiteUsername, gameData.getWhiteColor());
//        Assertions.assertEquals(blackUsername, gameData.getBlackColor());
//        Assertions.assertEquals(gameName, gameData.getGameName());
//    }
//
//    @Test
//    void testGetAllGameData() {
//        // Arrange
//        gameSQLDao.addNewGame(1, "Game 1");
//        gameSQLDao.addNewGame(2, "Game 2");
//
//        // Act
//        Collection<GameData> games = gameSQLDao.getAllGameData();
//
//        // Assert
//        Assertions.assertEquals(2, games.size());
//    }
//}

//    @Test
//    public void testAddUser() throws DataAccessException {
//        UserData testUser = new UserData("newbuddy", "testPass", "test@example.com");
//        UserSQLDao userSQLDao = new UserSQLDao();
//        userSQLDao.clearuserdatabase();
//        userSQLDao.adduserdata(testUser);
//        UserData retrievedUser = userSQLDao.getuserdata("newbuddy");
//        assertNotNull(retrievedUser);
//        assertEquals("newbuddy", retrievedUser.username());
//        assertEquals("test@example.com", retrievedUser.email());
//        userSQLDao.clearuserdatabase();
//    }


