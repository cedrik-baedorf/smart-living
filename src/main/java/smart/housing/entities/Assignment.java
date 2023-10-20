package smart.housing.entities;

import javax.persistence.*;

/**
 * This entity represents one row of the table <i>Assignment</i>
 */
@Entity
@Table(name= "assignment")
public class Assignment {

    /**
     * Unique id of the assignment
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private int assignmentID;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task taskID;

    @Column(name = "username")
    private String username;

    public Assignment(Task task, String username){
        this.taskID = task;
        this.username = username;
    }

    public Assignment() {

    }
}
