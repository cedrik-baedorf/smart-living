package smart.housing.services;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.IncorrectCredentialsException;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.security.HashAlgorithm;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementServiceImplementationTest {

    private DatabaseConnector login_mockDatabaseConnector() {
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
    public void testLogin_unknownUsername() {
        String username = "unknown";
        String password = "password";

        UserManagementService userManagementService = new UserManagementServiceImplementation(login_mockDatabaseConnector(), null);

        assertThrowsExactly(IncorrectCredentialsException.class, () -> userManagementService.login(username, password));
    }

    /**
     * Test <code>login(String username, String password): {@link User}</code> method
     * with hashed parameter <code>password</code> that does not match the <code>user.getPassword()</code>
     * of the {@link User} object with <code>user.getUsername() == username</code>
     */
    @Test
    public void testLogin_incorrectPassword() {
        String username = "username";
        String password = "12345678";

        UserManagementService userManagementService = new UserManagementServiceImplementation(login_mockDatabaseConnector(), null);

        assertThrowsExactly(IncorrectCredentialsException.class, () -> userManagementService.login(username, password));
    }

    /**
     * Test <code>login(String username, String password): {@link User}</code> method
     * with hashed parameter <code>password</code> that matches the <code>user.getPassword()</code>
     * of the {@link User} object with <code>user.getUsername() == username</code>
     */
    @Test
    public void testLogin_correctCredentials() {
        String username = "username";
        String password = "password";

        UserManagementService userManagementService = new UserManagementServiceImplementation(login_mockDatabaseConnector(), null);

        User expectedUser = new User(username, password, UserManagementServiceImplementation.HASH_ALGORITHM);

        assertEquals(expectedUser, userManagementService.login(username, password));
    }

    private DatabaseConnector create_mockDatabaseConnector() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);

        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.doNothing().when(entityManager).close();
        Mockito.when(entityManager.find(User.class, "standard")).thenReturn(new User("standard"));

        DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);

        return databaseConnector;
    }

    /**
     * This method returns a mocked {@link User} object where
     * <code>User.getRank().outranks(UserRole role)</code> always returns <code>true</code>
     */
    private User mockHighestRankedUser() {
        UserRole role = Mockito.mock(UserRole.class);
        Mockito.when(role.outranks(Mockito.any(UserRole.class))).thenReturn(true);
        Mockito.when(role.outranks(null)).thenReturn(true);

        User user = Mockito.mock(User.class);
        Mockito.when(user.getRole()).thenReturn(role);

        return user;
    }

    /**
     * This method returns a mocked {@link User} object where
     * <code>User.getRank().outranks(UserRole role)</code> always returns <code>false</code>
     */
    private User mockLowestRankedUser() {
        UserRole role = Mockito.mock(UserRole.class);
        Mockito.when(role.outranks(Mockito.any(UserRole.class))).thenReturn(false);
        Mockito.when(role.outranks(null)).thenReturn(false);

        User user = Mockito.mock(User.class);
        Mockito.when(user.getRole()).thenReturn(role);

        return user;
    }

    /**
     * Test <code>create(User user): void</code> method with parameter <code>user = null</code>
     */
    @Test
    public void testCreate_userIsNull() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(create_mockDatabaseConnector(), mockHighestRankedUser());

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.create(null));
    }

    /**
     * Test <code>create(User user): void</code> method with an already existing username
     */
    @Test
    public void testCreate_usernameAlreadyExists() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(create_mockDatabaseConnector(), mockHighestRankedUser());

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.create(new User("standard")));
    }

    /**
     * Test <code>create(User user): void</code> method with <code>user.getPassword() = null</code>
     */
    @Test
    public void testCreate_passwordEqualNull() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(create_mockDatabaseConnector(), mockHighestRankedUser());

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.create(new User("standard")));
    }

    /**
     * Test <code>create(User user): void</code> method with a correct <code>User</code> object
     */
    @Test
    public void testCreate_persistCorrectUser() {
        DatabaseConnector databaseConnector = create_mockDatabaseConnector();
        UserManagementService userManagementService = new UserManagementServiceImplementation(databaseConnector, mockHighestRankedUser());

        HashAlgorithm hashAlgorithm = UserManagementServiceImplementation.HASH_ALGORITHM;

        User persistedUser = new User("username", "password", hashAlgorithm);

        assertDoesNotThrow(() -> userManagementService.create(persistedUser));

        EntityManager entityManager = databaseConnector.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        InOrder inOrder = Mockito.inOrder(entityManager, entityTransaction);

        inOrder.verify(entityTransaction).begin();
        inOrder.verify(entityManager).persist(persistedUser);
        inOrder.verify(entityTransaction).commit();
        inOrder.verify(entityManager).close();
    }

    @Test
    public void testCreate_lowerRankThrowsException() {
        DatabaseConnector databaseConnector = create_mockDatabaseConnector();
        UserManagementService userManagementService = new UserManagementServiceImplementation(databaseConnector, mockLowestRankedUser());

        HashAlgorithm hashAlgorithm = UserManagementServiceImplementation.HASH_ALGORITHM;

        User persistedUser = new User("username", "password", hashAlgorithm);

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.create(persistedUser));
    }

}