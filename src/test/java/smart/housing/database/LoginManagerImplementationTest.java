package smart.housing.database;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import smart.housing.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.*;

public class LoginManagerImplementationTest {

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

    @Test
    public void testCreate_userIsNull() {
        LoginManager loginManager = new LoginManagerImplementation(mockDatabaseConnector());

        assertThrows(RuntimeException.class, () -> loginManager.create(null));
    }

    @Test
    public void testCreate_usernameIsNull() {
        LoginManager loginManager = new LoginManagerImplementation(mockDatabaseConnector());

        assertThrows(RuntimeException.class, () -> loginManager.create(new User(null)));
    }

    @Test
    public void testCreate_usernameAlreadyExists() {
        LoginManager loginManager = new LoginManagerImplementation(mockDatabaseConnector());

        assertThrows(RuntimeException.class, () -> loginManager.create(new User("standard")));
    }

    @Test
    public void testCreate_persistCorrectUser() {
        DatabaseConnector databaseConnector = mockDatabaseConnector();
        LoginManager loginManager = new LoginManagerImplementation(databaseConnector);

        User persistedUser = new User("username");

        assertDoesNotThrow(() -> loginManager.create(persistedUser));

        EntityManager entityManager = databaseConnector.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        InOrder inOrder = Mockito.inOrder(entityManager, entityTransaction);

        inOrder.verify(entityTransaction).begin();
        inOrder.verify(entityManager).persist(persistedUser);
        inOrder.verify(entityTransaction).commit();
    }

}