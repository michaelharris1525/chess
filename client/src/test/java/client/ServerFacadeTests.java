package client;

import model.GameData;
import org.junit.jupiter.api.*;
import requestextension.ResponseException;
import server.Server;
import ui.ResponseSuccess;
import ui.ServerFacade;

import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private ServerFacade serverFacade;
    private static int port;
    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }
    @BeforeEach
    void setUp() throws ResponseException {
        serverFacade = new ServerFacade("http://localhost:" + port); // Mock or test server URL
        serverFacade.clearDatabase();
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void testRegister_Positive() throws ResponseException {
        String username = "newUser";
        String password = "newPassword";
        String email = "user@example.com";

        ResponseSuccess response = serverFacade.register(username, password, email);
        assertNotNull(response, "Response should not be null for successful registration");
        assertNotNull(response.getAuthToken(), "Auth token should not be null after registration");
    }

    @Test
    void testRegister_Negative() {
        // Simulate registration with missing email
        String password = "password";

        assertThrows(ResponseException.class, () -> serverFacade.register(null, password, null),
                "Should throw ResponseException for invalid registration data");
    }

    @Test
    void testLogout_Positive() throws ResponseException {
        // Test that logout does not throw exceptions (assumes auth is stored)
        String username = "newUdfaser";
        String password = "newPasdasfadsfsword";
        String email = "user@examplasdfasdfe.com";

        ResponseSuccess res = serverFacade.register(username, password, email);
        assertDoesNotThrow(() -> serverFacade.logout(), "Logout should not throw exceptions");
    }
    @Test
    void testLogin_Positive() throws ResponseException {
        // Simulate valid login credentials
        String username = "testUser";
        String password = "testPasword";
        String email = "mail";
        ResponseSuccess dsfresponse = serverFacade.register(username, password, email);

        ResponseSuccess response = serverFacade.login(username, password);
        assertNotNull(response, "Response should not be null for valid login");
        assertNotNull(response.getAuthToken(), "Auth token should not be null");
    }

    @Test
    void testLogin_Negative() {
        // Simulate invalid login credentials
        String username = "invalidUser";
        String password = "invalidPassword";

        assertThrows(ResponseException.class, () -> serverFacade.login(username, password),
                "Should throw ResponseException for invalid login credentials");
    }

    @Test
    void testClientUserCreateGame_Positive() throws ResponseException {
        String username = "testUsedsr";
        String password = "testPafsdsword";
        String email = "masil";
        ResponseSuccess resp = serverFacade.register(username, password, email);
        String gameName = "Test Game";
        assertDoesNotThrow(() -> serverFacade.clientuserCreateGame(gameName),
                "Creating a game with a valid name should not throw exceptions");
    }

    @Test
    void testClientUserCreateGame_Negative() {
        // Simulate creating a game without a name
        String invalidGameName = "";

        assertThrows(ResponseException.class, () -> serverFacade.clientuserCreateGame(invalidGameName),
                "Should throw ResponseException for invalid game name");
    }

    @Test
    void testListAllGames_Positive() throws ResponseException {
        String username = "testUser";
        String password = "testPasword";
        String email = "mail";
        ResponseSuccess dfa = serverFacade.register(username, password, email);
        serverFacade.clientuserCreateGame("GAMENAME");
        Map<String, Collection<GameData>> gameList = serverFacade.flistAllGames();
        assertNotNull(gameList, "Game list should not be null for valid request");
        assertTrue(gameList.size() >= 0, "Game list should be empty or contain games");
    }

    @Test
    void testListAllGames_Negative() {
        // Simulate a server failure
        assertThrows(ResponseException.class, serverFacade::flistAllGames,
                "Should throw ResponseException for server failure or invalid request");
    }

    @Test
    void testObserveID_Positive() throws ResponseException {
        int gameId = 123; // Assume this is a valid game ID
        assertDoesNotThrow(() -> serverFacade.observeID(gameId),
                "Observing a valid game ID should not throw exceptions");
    }

}
