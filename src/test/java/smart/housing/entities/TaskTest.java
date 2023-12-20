package smart.housing.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTask_taskNameIsNull(){
        assertThrows(RuntimeException.class, () -> new Task(null));
    }

    @Test
    public void testTask_assigneesIsNull(){
        assertThrows(RuntimeException.class, () -> new Task("test", null));
    }

    @Test
    public void testMarkAsCompleted_WithAssignee() {
        Task task = new Task();
        User roommate = new User("Anna");
        task.addAssignee(roommate);
        task.markAsCompleted();
        assertTrue(task.getCompleted());
    }

    @Test
    public void testMarkAsCompleted_WithoutAssignee(){
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

    @Test
    public void testSetAssignees() {
        Task task = new Task("test");
        Set<User> assignees = new HashSet<>();
        User roommate1 = new User("Anna");
        User roommate2 = new User("Tom");
        assignees.add(roommate1);
        assignees.add(roommate2);
        task.setAssignees(assignees);
        assertEquals(assignees, task.getAssignees());
    }

    @Test
    public void testAddAssignee() {
        Task task = new Task("test");
        User roommate = new User("Anna");
        task.addAssignee(roommate);
        assertTrue(task.getAssignees().contains(roommate));
    }

    @Test
    public void testToString() {
        Task task = new Task("test");
        task.setDueDate(LocalDate.now().plusDays(7));
        task.setCompleted(true);
        assertEquals("(test; " + task.getDueDate() + "; completed)", task.toString());
    }
}