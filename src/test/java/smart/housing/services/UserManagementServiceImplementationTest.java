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
     * the method <code>create(user: {@link User})</code> of class {@link UserManagementServiceImplementation}
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
     * the method <code>modify(user: {@link User}, modifiedUser: {@link User})</code> of class {@link UserManagementServiceImplementation}
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
     * This static method creates a mocked {@link DatabaseConnector} object used for the tests for
     * the method <code>delete(user: {@link User})</code> of class {@link UserManagementServiceImplementation}
     * @return mocked {@link DatabaseConnector}
     */
    private static DatabaseConnector delete_mockDatabaseConnector() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);

        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.doNothing().when(entityManager).close();

        Mockito.when(entityManager.merge(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.doNothing().when(entityManager).remove(Mockito.any(User.class));

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
     * Test <code>create(user: {@link User}): void</code> method with parameter <code>user = null</code>
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
     * Test <code>create(user: {@link User}): void</code> method with <code>user.getPassword() = null</code>
     */
    @Test
    public void testCreate_passwordEqualNull() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(create_mockDatabaseConnector(), mockHighestRankedUser());

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.create(new User("standard")));
    }

    /**
     * Test <code>create(user: {@link User}): void</code> method with a correct <code>User</code> object
     */
    @Test
    public void testCreate_persistCorrectUser() {
        DatabaseConnector databaseConnector = create_mockDatabaseConnector();
        UserManagementService userManagementService = new UserManagementServiceImplementation(databaseConnector, mockHighestRankedUser());

        User persistedUser = createCompleteUser();

        assertDoesNotThrow(() -> userManagementService.create(persistedUser));

        EntityManager entityManager = databaseConnector.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        InOrder inOrder = Mockito.inOrder(entityManager, entityTransaction);

        inOrder.verify(entityTransaction).begin();
        inOrder.verify(entityManager).persist(persistedUser);
        inOrder.verify(entityTransaction).commit();
        inOrder.verify(entityManager).close();
    }

    /**
     * Test <code>create(user: {@link User}): void</code> method with user of higher role than the service user
     */
    @Test
    public void testCreate_lowerRankThrowsException() {
        DatabaseConnector databaseConnector = create_mockDatabaseConnector();
        UserManagementService userManagementService = new UserManagementServiceImplementation(databaseConnector, mockLowestRankedUser());

        HashAlgorithm hashAlgorithm = UserManagementServiceImplementation.HASH_ALGORITHM;

        User persistedUser = new User("username", "password", hashAlgorithm);

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.create(persistedUser));
    }

    /**
     * Test <code>modify(user: {@link User}, modifiedUser: {@link User}): void</code> method with parameter <code>user = null</code>.
     */
    @Test
    public void testModify_userIsNull() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(modify_mockDatabaseConnector(), mockHighestRankedUser());

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.modify(null, createCompleteUser()));
    }

    /**
     * Test <code>modify(user: {@link User}, modifiedUser: {@link User}): void</code> method with parameter <code>modifiedUser = null</code>.
     */
    @Test
    public void testModify_modifiedUserIsNull() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(modify_mockDatabaseConnector(), mockHighestRankedUser());

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.modify(createCompleteUser(), null));
    }

    /**
     * Test <code>modify(user: {@link User}, modifiedUser: {@link User}): void</code> with all attributes but <code>username</code> modified
     */
    @Test
    public void testModify_correctModification() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(modify_mockDatabaseConnector(), mockHighestRankedUser());

        User user = createCompleteUser();

        User modifiedUser = new User("other");
        modifiedUser.setFirstName("Other First Name");
        modifiedUser.setLastName("Other Last Name");
        modifiedUser.setRole(UserRole.SUPREME);
        modifiedUser.setEmail("other.email@address.com");
        modifiedUser.setPassword("different password", UserManagementServiceImplementation.HASH_ALGORITHM);

        assertDoesNotThrow(() -> userManagementService.modify(user, modifiedUser));

        User expectedUser = new User(user.getUsername());
        expectedUser.setFirstName(modifiedUser.getFirstName());
        expectedUser.setLastName(modifiedUser.getLastName());
        expectedUser.setRole(modifiedUser.getRole());
        expectedUser.setEmail(modifiedUser.getEmail());
        expectedUser.setPasswordHash(modifiedUser.getPassword());

        assertEquals(expectedUser, user);
    }

    /**
     * Test <code>modify(user: {@link User}, modifiedUser: {@link User}): void</code> method with user of higher role than the service user
     */
    @Test
    public void testModify_lowerRankThrowsException() {
        DatabaseConnector databaseConnector = modify_mockDatabaseConnector();
        UserManagementService userManagementService = new UserManagementServiceImplementation(databaseConnector, mockLowestRankedUser());

        User user = createCompleteUser();
        User modifiedUser = createCompleteUser();
        modifiedUser.setRole(UserRole.ADMIN);

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.modify(user, modifiedUser));
    }

    /**
     * Test <code>delete(user: {@link User}): void</code> method with parameter <code>user = null</code>.
     */
    @Test
    public void testDelete_userIsNull() {
        UserManagementService userManagementService = new UserManagementServiceImplementation(delete_mockDatabaseConnector(), mockHighestRankedUser());

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.delete(null));
    }

    /**
     * Test <code>delete(user: {@link User}): void</code> with correct circumstances
     */
    @Test
    public void testDelete_correctDeletion() {
        DatabaseConnector databaseConnector = delete_mockDatabaseConnector();
        UserManagementService userManagementService = new UserManagementServiceImplementation(databaseConnector, mockHighestRankedUser());

        User user = createCompleteUser();

        assertDoesNotThrow(() -> userManagementService.delete(user));

        EntityManager entityManager = databaseConnector.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        InOrder inOrder = Mockito.inOrder(entityManager, entityTransaction);

        inOrder.verify(entityTransaction).begin();
        inOrder.verify(entityManager).merge(user);
        inOrder.verify(entityManager).remove(user);
        inOrder.verify(entityTransaction).commit();
        inOrder.verify(entityManager).close();
    }

    /**
     * Test <code>delete(user: {@link User}): void</code> method with user of higher role than the service user
     */
    @Test
    public void testDelete_lowerRankThrowsException() {
        DatabaseConnector databaseConnector = delete_mockDatabaseConnector();
        UserManagementService userManagementService = new UserManagementServiceImplementation(databaseConnector, mockLowestRankedUser());

        User user = mockHighestRankedUser();

        assertThrowsExactly(UserManagementServiceException.class, () -> userManagementService.delete(user));
    }

}