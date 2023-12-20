package smart.housing.services;

import smart.housing.database.DatabaseConnector;
import smart.housing.entities.Task;
import smart.housing.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    public List<Task> getCurrentTasks(User activeUser) {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysFromNow = today.plusDays(7);
        TypedQuery<Task> typedQuery = entityManager.createNamedQuery(Task.FIND_WITH_FILTERS, Task.class);
        typedQuery.setParameter("isCompleted", false);
        typedQuery.setParameter("startDate", today);
        typedQuery.setParameter("endDate", sevenDaysFromNow);
        Set<User> userSet = Collections.singleton(activeUser);
        typedQuery.setParameter("assignee", userSet);
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
        if (newTask.getTaskName() == null || newTask.getTaskName().isEmpty())
            throw new RuntimeException("Task Name cannot be empty");
        LocalDate dueDate = newTask.getDueDate();
        if (dueDate == null)
            throw new RuntimeException("Due Date cannot be null");
        if (dueDate.isBefore(LocalDate.now()))
            throw new RuntimeException("Due Date cannot be in the past");
        if (newTask.getAssignees() == null || newTask.getAssignees().isEmpty())
            throw new RuntimeException("Task must have at least one assignee");

        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();

        try{
            entityManager.getTransaction().begin();
            entityManager.persist(newTask);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating task: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void modify(Task oldTask, Task updateTask) {
        if (updateTask.getTaskName() == null || updateTask.getTaskName().isEmpty())
            throw new RuntimeException("Task Name cannot be empty");
        LocalDate dueDate = updateTask.getDueDate();
        if (dueDate == null)
            throw new RuntimeException("Due Date cannot be null");
        if (dueDate.isBefore(LocalDate.now()))
            throw new RuntimeException("Due Date cannot be in the past");
        if (updateTask.getAssignees() == null || updateTask.getAssignees().isEmpty())
            throw new RuntimeException("Task must have at least one assignee");

        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            oldTask = entityManager.merge(oldTask);
            oldTask.setTaskName(updateTask.getTaskName());
            oldTask.setDueDate(updateTask.getDueDate());
            oldTask.setAssignees(updateTask.getAssignees());

            entityManager.getTransaction().commit();
        } catch (Exception e){
            e.getStackTrace();
        }
        entityManager.close();
    }

    @Override
    public void delete(Task task) {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            task = entityManager.merge(task);
            if (task == null)
                throw new IllegalArgumentException("Task not found in the database");
            entityManager.remove(task);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error deleting task: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void complete(Task task, Boolean status) {
        EntityManager entityManager = DATABASE_CONNECTOR.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Task mergedTask = entityManager.merge(task);
            if (mergedTask == null){
                throw new IllegalArgumentException("Task not found in the database");
            }
            mergedTask.setCompleted(status);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error completing task: " + e.getMessage());
        }
        finally {
            entityManager.close();
        }
    }
}
