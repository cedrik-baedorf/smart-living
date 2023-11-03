package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.Task;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class TaskManagementServiceImplementation implements TaskManagementService {

    private DatabaseConnector databaseConnector;

    public TaskManagementServiceImplementation(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public List<Task> getAllTasks() {
        EntityManager entityManager = databaseConnector.createEntityManager();
        List<Task> taskList = entityManager.createNamedQuery(Task.FIND_ALL, Task.class).getResultList();
        entityManager.close();
        return taskList;
    }

    @Override
    public List<Task> getCurrentTasks() {
        EntityManager entityManager = databaseConnector.createEntityManager();
        TypedQuery<Task> typedQuery = entityManager.createNamedQuery(Task.FIND_WITH_FILTERS, Task.class);
        entityManager.close();
        return typedQuery.getResultList();
    }
}
