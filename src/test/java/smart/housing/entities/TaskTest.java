package smart.housing.entities;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import smart.housing.security.HashAlgorithm;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testMarkAsCompleted_WithAssignee() {
        Task task = new Task();
        User roommate = new User("Anna");
        task.addAssignee(roommate);
        task.setCompleted();
        assertTrue(task.getCompleted());
    }

    @Test
    public void testMarkCompleted_WithoutAssignee(){
        Task task2 = new Task();
        task2.setCompleted();
        assertFalse(task2.getCompleted());
    }

}