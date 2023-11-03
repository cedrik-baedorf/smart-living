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
                query = "SELECT task FROM Task task"
        ),
        @NamedQuery(
                name = Task.FIND_WITH_FILTERS,
                query = """
                        SELECT task FROM Task task
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
     * Unique id of the task
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskID;

    /**
     * Name of the task
     */
    @NotNull
    @Column(name = "task_name")
    private String taskName;

    /**
     * To which roommate is the task assigned
     */
    @ManyToMany()
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
    private boolean isCompleted;

    public Task (String taskName, Set<User> assignees){
        this.taskName = taskName;
        this.assignees = assignees == null ? new HashSet<>() : assignees;
        this.isCompleted = false;
    }

    public Task (String taskName){
        this(taskName, new HashSet<>());
    }

    public Task() {
        this("unnamed Task");
    }

    public void addAssignee(User roommate){
        assignees.add(roommate);
    }

    public void removeAssignee(User roommate) {
        assignees.remove(roommate);
    }

    public void markAsCompleted(){
        if (this.getAssignees() == null || this.getAssignees().isEmpty())
            throw new IllegalStateException("A task must be assigned to at least one roommate before marking it as completed");
        else
            this.setCompleted();
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

    private void setCompleted(){
        isCompleted = true;
    }

    public boolean getCompleted() {
        return isCompleted;
    }

}
