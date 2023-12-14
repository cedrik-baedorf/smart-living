package smart.housing.services;

import smart.housing.entities.Task;
import smart.housing.entities.User;

import java.util.List;

public interface TaskManagementService {

    List<Task> getAllTasks();

    /**
     * This method shall return a {@link List} of type {@link Task} including all tasks which
     * - need to be done within the next 7 days
     * - are past due date and have not been completed yet
     * @return {@link List} of type {@link Task} meeting the criteria above
     */
    List<Task> getCurrentTasks(User activeUser);

    List<Task> getIncompleteTasks();
    void create(Task newTask);
    void modify(Task oldTask, Task updateTask);
    void delete(Task task);
    void complete (Task task, Boolean status);
}
