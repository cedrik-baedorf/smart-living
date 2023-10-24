package smart.housing.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testMarkAsCompleted_WithAssignee() {
        Task task = new Task();
        User roommate = new User("Anna");
        task.addAssignee(roommate);
        task.markAsCompleted();
        assertTrue(task.getCompleted());
    }

    @Test
    public void testMarkCompleted_WithoutAssignee(){
        Task task = new Task();
        assertThrows(IllegalStateException.class, task::markAsCompleted);
    }

    @Test
    public void testMarkAsCompleted() {
        Task task = new Task();
        User roommate = new User("Anna");
        task.addAssignee(roommate);
        assertFalse(task.getCompleted());
    }

    @Test
    public void testGetCompleted_WithoutAssignee(){
        Task task = new Task();
        assertFalse(task.getCompleted());
    }




}