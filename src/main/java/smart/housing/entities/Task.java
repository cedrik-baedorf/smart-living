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
                query = "SELECT task FROM Task task"
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
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "assignments",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "username")
    )
    private Set<User> assignees;

    /**
     * Start date of this reoccurring task
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * Days between two task occurrences (7 = weekly)
     */
    @Column(name = "reoccurrence")
    private int reoccurrence;

    /**
     * End date of this reoccurring task (no task later than this date)
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Information about whether the task is completed or not
     */
    @Column(name = "completed")
    private boolean isCompleted;

    public Task (String taskName, Set<User> assignees){
        this.taskName = taskName;
        this.assignees = assignees;
        this.startDate = LocalDate.now();
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
        if (this.getAssignees().isEmpty())
            throw new IllegalStateException("A task must be completed to at least one roommate before marking it as completed");
        else
            this.setCompleted();

            // MISSING: database update to mark task as completed
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

    public LocalDate getStartDate(){
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getReoccurrence(){
        return this.reoccurrence;
    }

    public void setReoccurrence(int reoccurrence){
        this.reoccurrence = reoccurrence;
    }

    public LocalDate getEndDate(){
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }

    public void setCompleted(){
        isCompleted = true;
    }

    public boolean getCompleted() {
        return assignees != null && !assignees.isEmpty() && isCompleted;
    }

}
