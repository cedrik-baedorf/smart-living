package smart.housing.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity represents one row of table <i>Tasks</i>
 */
@Entity
@Table(name= "tasks")
@NamedQueries({
        @NamedQuery(
                name = Task.FIND_ALL,
                query = """
                        SELECT task FROM Task task
                        """
        ),
        @NamedQuery(
                name = Task.FIND_WITH_FILTERS,
                query = """
                        SELECT task FROM Task task
                        WHERE task.completed = coalesce(:isCompleted, task.completed)
                        AND task.dueDate >= :startDate
                        AND task.dueDate <= :endDate
                        AND :assignee MEMBER OF task.assignees
                        """
        ),
        @NamedQuery(
                name = Task.FIND_INCOMPLETE,
                query = """
                        SELECT task FROM Task task
                        WHERE task.completed = coalesce(:isCompleted, task.completed)
                        """
        )
})

public class Task {

    /**
     * Name of named query to return all tasks
     */
    public static final String FIND_ALL = "Task.findAll";

    /**
     * Name of named query to return all tasks filtered by
     */
    public static final String FIND_WITH_FILTERS = "Task.findWithFilters";

    /**
     * Name of named query to return all tasks that have not been completed yet
     */

    public static final String FIND_INCOMPLETE = "Task.findIncomplete";

    /**
     * Unique id of the task
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskID;

    /**
     * Name of the task
     */
    @NotNull
    @Column(name = "task_name")
    private String taskName;

    /**
     * To which roommate is the task assigned
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "assignments",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "username")
    )
    private Set<User> assignees;

    /**
     * Due date of this reoccurring task
     */
    @Column(name = "due_date")
    private LocalDate dueDate;

    /**
     * Information about whether the task is completed or not
     */
    @Column(name = "completed")
    private boolean completed;

    public Task (String taskName, Set<User> assignees){
        if (taskName == null)
            throw new RuntimeException("Task Name cannot be null");
        if (assignees == null)
            throw new RuntimeException("Assignees cannot be null");
        this.taskName = taskName;
        this.assignees = assignees == null ? new HashSet<>() : assignees;
        this.completed = false;
    }

    public Task (String taskName){
        this(taskName, new HashSet<>());
    }

    public Task() {
        this("unnamed Task");
    }

    public void setAssignees(Set<User> assignees) {
        this.assignees.clear();
        this.assignees.addAll(assignees);
    }

    public void addAssignee(User roommate){
        assignees.add(roommate);
    }

    public void markAsCompleted(){
        if (this.getAssignees() == null || this.getAssignees().isEmpty())
            throw new IllegalStateException("A task must be assigned to at least one roommate before marking it as completed");
        else
            this.setCompleted(true);
    }
    public boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getTaskName(){
        return this.taskName;
    }

    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public Set<User> getAssignees() {
        return assignees;
    }

    public LocalDate getDueDate(){
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String toString(){
        return "(" + taskName + "; " + dueDate + "; " + (completed ? "completed" : "not completed") + ")";
    }

    public Object getId() {
        return this.taskID;
    }

    public void setId(int i) {
        this.taskID = i;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass().equals(getClass())) {
            Task task = (Task) obj;
            return
                taskName != null && taskName.equals(task.getTaskName()) &&
                dueDate != null && dueDate.equals(task.getDueDate()) &&
                assignees != null && assignees.equals(task.getAssignees()) &&
                completed == task.getCompleted();
        }
        return false;
    }

}
