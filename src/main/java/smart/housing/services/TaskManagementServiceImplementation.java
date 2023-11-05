package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.database.DatabaseConnectorImplementation;
import smart.housing.entities.Task;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
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
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysFromNow = today.plusDays(7);
        TypedQuery<Task> typedQuery = entityManager.createNamedQuery(Task.FIND_WITH_FILTERS, Task.class);
        typedQuery.setParameter("isCompleted", false);
        typedQuery.setParameter("startDate", today);
        typedQuery.setParameter("endDate", sevenDaysFromNow);
        List<Task> currentTaskList = typedQuery.getResultList();
        entityManager.close();
        return currentTaskList;
    }

    @Override
    public List<Task> getIncomleteTasks() {
        EntityManager entityManager = databaseConnector.createEntityManager();
        TypedQuery<Task> typedQuery = entityManager.createNamedQuery(Task.FIND_INCOMPLETE, Task.class);
        typedQuery.setParameter("isCompleted", false);
        List<Task> currentTaskList = typedQuery.getResultList();
        entityManager.close();
        return currentTaskList;
    }

    public static void main(String [] args){
        TaskManagementService service = new TaskManagementServiceImplementation(new DatabaseConnectorImplementation());
        List<Task> list = service.getCurrentTasks();
        list.forEach(System.out::println);
    }
}
