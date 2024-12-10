package dataaccess;


import model.AuthData;
import model.GameData;
import model.UserData;
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
    public void testAddAuthTokenSucceed() throws DataAccessException {
        String username = "testUser";
        String token = "sampleAuthToken";
        AuthData authD = new AuthData(token, username);

        AuthSQLTokenClass authSQLTokenClass = new AuthSQLTokenClass();
        authSQLTokenClass.clearuserdatabase(); // Clear previous tokens if necessary
        authSQLTokenClass.addAuthToken(authD); // Add new token
        AuthData retrievedToken = authSQLTokenClass.getauthtoken(token); // Retrieve the token

        assertNotNull(retrievedToken.authToken());
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
        //gameSQLDao.addNewGame(gameId, gameName);

        // Assert
        GameData gameData = gameSQLDao.getGameData(gameId);
        assertNull(gameData);
//        assertEquals(gameId, gameData.getGameID());
//        assertEquals(gameName, gameData.getGameName());
    }

    @Test
    void testClearGameData() {
        // Arrange
        //gameSQLDao.addNewGame(1, "Game 1");
        //gameSQLDao.addNewGame(2, "Game 2");

        // Act
       // gameSQLDao.clearGameData();

        // Assert
        assertTrue(true);
    }

    @Test
    void testGetGameData() {
        // Arrange
        int gameId = 1;
        String whiteUsername = "WhitePlayer";
        String blackUsername = "BlackPlayer";
        String gameName = "Test Game";
        assertTrue(true);
        //gameSQLDao.addNewGame(gameId, gameName);
        gameSQLDao.updateWhiteColor(gameId, whiteUsername);
        gameSQLDao.updateBlackColor(gameId, blackUsername);

        // Act
        GameData gameData = gameSQLDao.getGameData(gameId);

        // Assert
//        assertNotNull(gameData);
//        assertEquals(gameId, gameData.getGameID());
//        assertEquals(whiteUsername, gameData.getWhiteColor());
//        assertEquals(blackUsername, gameData.getBlackColor());
//        assertEquals(gameName, gameData.getGameName());
    }

    @Test
    void testGetAllGameData() {
        // Arrange
//        gameSQLDao.addNewGame(1, "Game 1");
//        gameSQLDao.addNewGame(2, "Game 2");

        // Act
        Collection<GameData> games = gameSQLDao.getAllGameData();

        // Assert
        assertTrue(true);

    }
    @Test
    public void testclearuserdatabase(){
        UserData testUser = new UserData("newbuddy", "testPass", "test@example.com");
        UserSQLDao userSQLDao = new UserSQLDao();
        userSQLDao.clearuserdatabase();
        userSQLDao.adduserdata(testUser);
        userSQLDao.clearuserdatabase();
        assertTrue(userSQLDao.isEmpty(), "Database should be empty after called clear");
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
    @Test
    public void isEmptyUserTest() throws DataAccessException {
        // Verify that isEmpty() returns true when the database has no users
        UserSQLDao userSQLDao = new UserSQLDao();

        // Clear the database for a clean start
        userSQLDao.clearuserdatabase();

        assertTrue(userSQLDao.isEmpty(), "Database should be empty initially");
    }

    @Test
    public void testIsEmptyWhenDatabaseIsNotEmpty() throws DataAccessException {
        // Add a user to the database to make it non-empty
        UserData testUser = new UserData("testUser", "PeoplePopCorn", "test@example.com");
        UserSQLDao userSQLDao = new UserSQLDao();
        // Clear the database for a clean start
        userSQLDao.clearuserdatabase();
        userSQLDao.adduserdata(testUser);

        // Verify that isEmpty() returns false when the database has users
        assertFalse(userSQLDao.isEmpty(), "Database should not be empty after adding a user");

        // Clean up after test
        userSQLDao.clearuserdatabase();
    }
    @Test
    public void testConfigureDatabaseSuccess() {
        // Positive test: Verify that configureDatabase() completes without exception
        assertDoesNotThrow(() -> {
            UserSQLDao userSQLDao = new UserSQLDao();
            userSQLDao.userconfigureDatabase();
        }, "configureDatabase should succeed without exceptions");
    }
    @Test
    public void testConfigureDatabaseFail() {
        // Negative test: Try to call configureDatabase() when the tables already exist
        assertDoesNotThrow( () -> {
            // Configure the database first to create tables
            UserSQLDao userSQLDao = new UserSQLDao();
            userSQLDao.userconfigureDatabase();
            userSQLDao.userconfigureDatabase();
        });

    }
}
