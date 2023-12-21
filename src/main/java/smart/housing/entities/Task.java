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
     * Due date of this reoccurring task.
     */
    @Column(name = "due_date")
    private LocalDate dueDate;

    /**
     * Information about whether the task is completed or not.
     */
    @Column(name = "completed")
    private boolean completed;

    /**
     * Default constructor for an object of this class setting the taskName to 'unnamed Task'.
     */
    public Task() {
        this("unnamed Task");
    }

    /**
     * Overloaded constructor accepting a value for the <code>taskName</code> property.
     * @param taskName name of the task
     */
    public Task (String taskName){
        this(taskName, new HashSet<>());
    }

    public Task (String taskName, Set<User> assignees){
        if (taskName == null)
            throw new RuntimeException("Task Name cannot be null");
        if (assignees == null)
            throw new RuntimeException("Assignees cannot be null");
        this.taskName = taskName;
        this.assignees = assignees == null ? new HashSet<>() : assignees;
        this.completed = false;
    }

    /**
     * Setter for the <code>assignees</code> attribute. Replaces all current assignees.
     * @param assignees assignees of this task
     */
    public void setAssignees(Set<User> assignees) {
        this.assignees.clear();
        this.assignees.addAll(assignees);
    }

    /**
     * Setter for the <code>assignees</code> attribute. Adds the new assignee.
     * @param roommate new assignees of this task
     */
    public void addAssignee(User roommate){
        assignees.add(roommate);
    }

    /**
     * Setter for the <code>completed</code> attribute. Sets the value of the attribute to <code>true</code>.
     */
    public void markAsCompleted(){
        if (this.getAssignees() == null || this.getAssignees().isEmpty())
            throw new IllegalStateException("A task must be assigned to at least one roommate before marking it as completed");
        else
            this.setCompleted(true);
    }

    /**
     * Getter for the <code>completed</code> attribute.
     * @return completion status of this task
     */
    public boolean getCompleted() {
        return this.completed;
    }

    /**
     * Setter for the <code>completed</code> attribute.
     * @param completed completion status of this task
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Getter for the <code>taskName</code> attribute.
     * @return name of this task
     */
    public String getTaskName(){
        return this.taskName;
    }

    /**
     * Setter for the <code>taskName</code> attribute.
     * @param taskName name of this task
     */
    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    /**
     * Getter for the <code>assignees</code> attribute.
     * @return assignees of this task
     */
    public Set<User> getAssignees() {
        return assignees;
    }

    /**
     * Getter for the <code>dueDate</code> attribute.
     * @return due date of this task
     */
    public LocalDate getDueDate(){
        return this.dueDate;
    }

    /**
     * Setter for the <code>duaDate</code> attribute.
     * @param dueDate due date of this task
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Getter for the <code>taskID</code> attribute.
     * @return id of this task
     */
    public Object getId() {
        return this.taskID;
    }

    /**
     * Setter for the <code>taskID</code> attribute.
     * @param taskID id of this task
     */
    public void setId(int taskID) {
        this.taskID = taskID;
    }

    @Override
    public String toString(){
        return "(" + taskName + "; " + dueDate + "; " + (completed ? "completed" : "not completed") + ")";
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
