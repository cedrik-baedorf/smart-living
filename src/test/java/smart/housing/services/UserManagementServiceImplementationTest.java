package smart.housing.services;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.User;
import smart.housing.enums.UserRole;
import smart.housing.exceptions.UserManagementServiceException;
import smart.housing.security.HashAlgorithm;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementServiceImplementationTest {

    /**
     * This static method creates a mocked {@link DatabaseConnector} object used for the tests for
     * the method <code>create(User user)</code> of class {@link UserManagementServiceImplementation}
     * @return mocked {@link DatabaseConnector}
     */
    private static DatabaseConnector create_mockDatabaseConnector() {
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
     * This static method creates a mocked {@link DatabaseConnector} object used for the tests for
     * the method <code>modify(User user, User modifiedUser)</code> of class {@link UserManagementServiceImplementation}
     * @return mocked {@link DatabaseConnector}
     */
    private static DatabaseConnector modify_mockDatabaseConnector() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);

        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.doNothing().when(entityManager).close();

        Mockito.when(entityManager.merge(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);

        return databaseConnector;
    }

    /**
     * This static method returns a mocked {@link User} object where
     * <code>User.getRank().outranks(UserRole role)</code> always returns <code>true</code>
     */
    private static User mockHighestRankedUser() {
        UserRole role = Mockito.mock(UserRole.class);
        Mockito.when(role.outranks(Mockito.any(UserRole.class))).thenReturn(true);
        Mockito.when(role.outranks(null)).thenReturn(true);

        User user = Mockito.mock(User.class);
        Mockito.when(user.getRole()).thenReturn(role);

        return user;
    }

    /**
     * This static method returns a mocked {@link User} object where
     * <code>User.getRank().outranks(UserRole role)</code> always returns <code>false</code>
     */
    private static User mockLowestRankedUser() {
        UserRole role = Mockito.mock(UserRole.class);
        Mockito.when(role.outranks(Mockito.any(UserRole.class))).thenReturn(false);
        Mockito.when(role.outranks(null)).thenReturn(false);

        User user = Mockito.mock(User.class);
        Mockito.when(user.getRole()).thenReturn(role);

        return user;
    }

    /**
     * This static method returns a {@link User} object where
     * all attributes have assigned values
     */
    private static User createCompleteUser() {
        User user = new User("username");
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setRole(UserRole.USER);
        user.setEmail("email@address.com");
        user.setPassword("password", UserManagementServiceImplementation.HASH_ALGORITHM);
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

    /**
     * Test <code>modify(user: User, modifiedUser: User): void</code> method with parameter <code>user = null</code>.
     */
    @Test
    public void testModify_userIsNull() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(modify_mockDatabaseConnector(), mockHighestRankedUser());

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.modify(null, createCompleteUser()));
    }

    /**
     * Test <code>modify(user: User, modifiedUser: User): void</code> method with parameter <code>modifiedUser = null</code>.
     */
    @Test
    public void testModify_modifiedUserIsNull() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(modify_mockDatabaseConnector(), mockHighestRankedUser());

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.modify(createCompleteUser(), null));
    }

    /**
     * Test <code>modify(user: User, modifiedUser: User): void</code> method with the parameter <code>modifiedUser</code>
     * having a different <code>username</code> attribute
     */
    @Test
    public void testModify_differentUsername() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(create_mockDatabaseConnector(), mockHighestRankedUser());

        User user = new User("username");
        user.setFirstName("First Name");
        User modifiedUser = new User("other");
        modifiedUser.setFirstName("Second Name");

        userManagementService.modify(user, modifiedUser);

        User expectedUser = user;
        expectedUser.setFirstName(modifiedUser.getFirstName());

        User actualUser = userManagementService.getUser("username");

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testModify_lowerRankThrowsException() {
        DatabaseConnector databaseConnector = modify_mockDatabaseConnector();
        UserManagementService userManagementService = new UserManagementServiceImplementation(databaseConnector, mockLowestRankedUser());

        User user = createCompleteUser();
        User modifiedUser = createCompleteUser();
        modifiedUser.setRole(UserRole.ADMIN);

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.modify(user, modifiedUser));
    }

}