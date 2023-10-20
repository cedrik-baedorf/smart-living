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
public class Task {

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
    @ManyToMany
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

    public void removeAssignee(User roomate) {
        assignees.remove(roomate);
    }

    public void markAsCompleted(){
        this.isCompleted = true;
    }

}