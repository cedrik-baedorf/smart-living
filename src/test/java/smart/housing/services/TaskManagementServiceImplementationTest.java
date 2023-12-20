package smart.housing.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.Task;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class TaskManagementServiceImplementationTest {

    public DatabaseConnector create_mockDatabaseConnector(){
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);

        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.doNothing().when(entityManager).close();
        Mockito.when(entityManager.find(Task.class, "test")).thenReturn(new Task("test"));

        DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);

        return databaseConnector;
    }

    @Test
    public void createNewTask_nameIsNull(){
        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(create_mockDatabaseConnector());

    }
}
