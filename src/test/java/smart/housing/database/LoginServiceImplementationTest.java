package smart.housing.database;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import smart.housing.entities.User;
import smart.housing.exceptions.LoginServiceException;
import smart.housing.security.HashAlgorithm;
import smart.housing.services.LoginService;
import smart.housing.services.LoginServiceImplementation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceImplementationTest {

    private DatabaseConnector mockDatabaseConnector() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);

        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.when(entityManager.find(User.class, "standard")).thenReturn(new User("standard"));

        DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);

        return databaseConnector;
    }

    /**
     * Test create(User user) method with parameter <code>user = null</code>>
     */
    @Test
    public void testCreate_userIsNull() {
        LoginService loginService = new LoginServiceImplementation(mockDatabaseConnector());

        assertThrowsExactly(LoginServiceException.class, () -> loginService.create(null));
    }

    /**
     * Test create(User user) method with an already existing username
     */
    @Test
    public void testCreate_usernameAlreadyExists() {
        LoginService loginService = new LoginServiceImplementation(mockDatabaseConnector());

        assertThrowsExactly(LoginServiceException.class, () -> loginService.create(new User("standard")));
    }

    /**
     * Test create(User user) method with <code>user.getPassword() = null</code>
     */
    @Test
    public void testCreate_passwordEqualNull() {
        LoginService loginService = new LoginServiceImplementation(mockDatabaseConnector());

        assertThrowsExactly(LoginServiceException.class, () -> loginService.create(new User("standard")));
    }

    /**
     * Test create(User user) method with a correct <code>User</code> object
     */
    @Test
    public void testCreate_persistCorrectUser() {
        DatabaseConnector databaseConnector = mockDatabaseConnector();
        LoginService loginService = new LoginServiceImplementation(databaseConnector);

        HashAlgorithm hashAlgorithm = Mockito.mock(HashAlgorithm.class);
        Mockito.when(hashAlgorithm.hash("password")).thenReturn("password");

        User persistedUser = new User("username", "password", hashAlgorithm);

        assertDoesNotThrow(() -> loginService.create(persistedUser));

        EntityManager entityManager = databaseConnector.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        InOrder inOrder = Mockito.inOrder(entityManager, entityTransaction);

        inOrder.verify(entityTransaction).begin();
        inOrder.verify(entityManager).persist(persistedUser);
        inOrder.verify(entityTransaction).commit();
    }

}