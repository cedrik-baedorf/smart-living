package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.database.DatabaseConnectorImplementation;
import smart.housing.entities.Task;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class TaskManagementServiceImplementation implements TaskManagementService {

    private final DatabaseConnector DATABASE_CONNECTOR;

    public TaskManagementServiceImplementation(DatabaseConnector databaseConnector) {
        this.DATABASE_CONNECTOR = databaseConnector;
    }

    @Override
    public List<Task> getAllTasks() {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        List<Task> taskList = entityManager.createNamedQuery(Task.FIND_ALL, Task.class).getResultList();
        entityManager.close();
        return taskList;
    }

    @Override
    public List<Task> getCurrentTasks() {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
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
    public List<Task> getIncompleteTasks() {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        TypedQuery<Task> typedQuery = entityManager.createNamedQuery(Task.FIND_INCOMPLETE, Task.class);
        typedQuery.setParameter("isCompleted", false);
        List<Task> currentTaskList = typedQuery.getResultList();
        entityManager.close();
        return currentTaskList;
    }

    @Override
    public void create(Task newTask) {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(newTask);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void main(String [] args){
        TaskManagementService service = new TaskManagementServiceImplementation(new DatabaseConnectorImplementation());
        List<Task> list = service.getCurrentTasks();
        list.forEach(System.out::println);
    }
}
