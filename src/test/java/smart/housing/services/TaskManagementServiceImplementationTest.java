package smart.housing.services;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import smart.housing.database.DatabaseConnector;
import smart.housing.entities.Task;
import smart.housing.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagementServiceImplementationTest {

    private static DatabaseConnector mockDatabaseConnector() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);

        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Mockito.doNothing().when(entityManager).close();

        Mockito.when(entityManager.merge(Mockito.any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            if(task.getTaskName().equals("Non-Existing Task"))
                throw new RuntimeException("Task not found in the database");
            return task;
        });

        Mockito.doNothing().when(entityManager).persist(Mockito.any(Task.class));
        Mockito.doNothing().when(entityManager).remove(Mockito.any(Task.class));

        DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
        Mockito.when(databaseConnector.createEntityManager()).thenReturn(entityManager);

        return databaseConnector;
    }

    @Test
    public void createNewTask_taskIsNull(){
        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(mockDatabaseConnector());
        assertThrowsExactly(NullPointerException.class, () -> taskManagementService.create(null));
    }

    @Test
    public void createNewTask_taskNameIsNull(){
        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(mockDatabaseConnector());
        assertThrowsExactly(RuntimeException.class, () -> taskManagementService.create(new Task(null)));
    }

    @Test
    public void createNewTask_dueDateIsNull(){
        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(mockDatabaseConnector());
        assertThrowsExactly(RuntimeException.class, () -> taskManagementService.create(new Task("test")));
    }

    @Test
    public void createNewTask_successfulCreation() {
        DatabaseConnector databaseConnector = mockDatabaseConnector();
        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(databaseConnector);

        Task newTask = new Task("New Task", Collections.singleton(new User("Ivan")));
        newTask.setDueDate(LocalDate.now().plusDays(1));

        assertDoesNotThrow(() -> taskManagementService.create(newTask));

        EntityManager entityManager = databaseConnector.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        InOrder inOrder = Mockito.inOrder(entityManager, entityTransaction);

        inOrder.verify(entityTransaction).begin();
        inOrder.verify(entityManager).persist(newTask);
        inOrder.verify(entityTransaction).commit();
        inOrder.verify(entityManager).close();
    }

    @Test
    public void modifyTask_successfulModification() {
        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(mockDatabaseConnector());

        Task oldTask = new Task("Old Task", new HashSet<>(Set.of(new User("Alice"))));
        oldTask.setDueDate(LocalDate.now().plusDays(2));

        Task updateTask = new Task("Updated Task", new HashSet<>(Set.of(new User("Bob"))));
        updateTask.setDueDate(LocalDate.now().plusDays(4));

        assertDoesNotThrow(() -> taskManagementService.modify(oldTask, updateTask));

        assertEquals(updateTask, oldTask);
    }

    @Test
    public void deleteTask_successfulDeletion(){
        DatabaseConnector databaseConnector = mockDatabaseConnector();
        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(databaseConnector);

        Task deleteTask = new Task("Delete Task");

        assertDoesNotThrow(() -> taskManagementService.delete(deleteTask));

        EntityManager entityManager = databaseConnector.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        InOrder inOrder = Mockito.inOrder(entityManager, entityTransaction);

        inOrder.verify(entityTransaction).begin();
        inOrder.verify(entityManager).merge(deleteTask);
        inOrder.verify(entityManager).remove(deleteTask);
        inOrder.verify(entityTransaction).commit();
        inOrder.verify(entityManager).close();
    }

    @Test
    public void completeTask_successfulCompletion() {
        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(mockDatabaseConnector());

        Task task = new Task("Test Task");
        task.setDueDate(LocalDate.now().plusDays(3));
        task.addAssignee(new User("Tom"));

        taskManagementService.create(task);

        assertDoesNotThrow(() -> taskManagementService.complete(task, true));

        assertTrue(task.getCompleted());
    }

    @Test
    public void completeTask_taskNotFound() {
        DatabaseConnector databaseConnector = mockDatabaseConnector();
        TaskManagementService taskManagementService = new TaskManagementServiceImplementation(databaseConnector);

        Task nonExistingTask = new Task("Non-Existing Task");
        nonExistingTask.setDueDate(LocalDate.now().plusDays(7));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskManagementService.complete(nonExistingTask, true));
        assertEquals("Error completing task: Task not found in the database", exception.getMessage());
    }
}
