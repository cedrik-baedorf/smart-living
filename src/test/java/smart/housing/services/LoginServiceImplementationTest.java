package smart.housing.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.security.HashAlgorithm;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceImplementationTest {

    private DatabaseConnector mockDatabaseConnector() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);

        HashAlgorithm hashAlgorithm = UserManagementServiceImplementation.HASH_ALGORITHM;

        User user = new User("username", "password", hashAlgorithm);

        Mockito.doNothing().when(entityManager).close();
        Mockito.when(entityManager.find(User.class, "username")).thenReturn(user);
        Mockito.when(entityManager.find(User.class, "unknown")).thenReturn(null);

        DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);

        return databaseConnector;
    }

    /**
     * Test <code>login(String username, String password): {@link User}</code> method
     * with parameter <code>username</code> that does not match any
     * {@link User} object known to the mocked {@link EntityManager}
     */
    @Test
    public void userLogin_unknownUsername() {
        String username = "unknown";
        String password = "password";

        LoginService loginService = new LoginServiceImplementation(mockDatabaseConnector());

        assertThrowsExactly(IncorrectCredentialsException.class, () -> loginService.userLogin(username, password));
    }

    /**
     * Test <code>login(String username, String password): {@link User}</code> method
     * with hashed parameter <code>password</code> that does not match the <code>user.getPassword()</code>
     * of the {@link User} object with <code>user.getUsername() == username</code>
     */
    @Test
    public void userLogin_incorrectPassword() {
        String username = "username";
        String password = "12345678";

        LoginService loginService = new LoginServiceImplementation(mockDatabaseConnector());

        assertThrowsExactly(IncorrectCredentialsException.class, () -> loginService.userLogin(username, password));
    }

    /**
     * Test <code>login(String username, String password): {@link User}</code> method
     * with hashed parameter <code>password</code> that matches the <code>user.getPassword()</code>
     * of the {@link User} object with <code>user.getUsername() == username</code>
     */
    @Test
    public void userLogin_correctCredentials() {
        String username = "username";
        String password = "password";

        LoginService loginService = new LoginServiceImplementation(mockDatabaseConnector());

        User expectedUser = new User(username, password, UserManagementServiceImplementation.HASH_ALGORITHM);

        assertEquals(expectedUser, loginService.userLogin(username, password));
    }

}