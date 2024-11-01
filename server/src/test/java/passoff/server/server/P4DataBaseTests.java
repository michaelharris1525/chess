package passoff.server.server;

import dataaccess.DataAccessException;
import dataaccess.UserSQLDao;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class P4DataBaseTests {

    @Test
    public void testAddUser() throws DataAccessException {
        UserData testUser = new UserData("testUser", "testPass", "test@example.com");
        UserSQLDao userSQLDao = new UserSQLDao();
        userSQLDao.adduserdata(testUser);

        UserData retrievedUser = userSQLDao.getuserdata("testUser");
        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.username());
        assertEquals("test@example.com", retrievedUser.email());
    }
}
