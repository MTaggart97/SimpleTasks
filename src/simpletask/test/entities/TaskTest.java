package simpletask.test.entities;

import simpletask.main.entities.Task;
import simpletask.main.entities.Action;
import simpletask.main.entities.InvalidPriorityException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class to test the behaviour of a Test instance.
 */
public class TaskTest {
    /**
     * Base task instance to test.
     */
    private Task t1;

    /**
     * Executes before each test method. Simply resets the t1 Task instance
     * to be empty.
     */
    @BeforeEach
    public void setUp() {
        t1 = new Task("Workspace");
    }
    /**
     * Executed after each tests. Sets the t1 Task instance to null to ensure
     * no data is carried over to another test.
     */
    @AfterEach
    public void tearDown() {
        t1 = null;
    }
    /**
     * Tests to see if an empty workspace has zero tasks associated with it.
     */
    @Test
    public void testCreateWorkspaceZero() {
        // Arrange
        // Act
        // Assert
        assertEquals(0, t1.getTasks().size(), "Workspace should contain no tasks on initialisation");
    }
    /**
     * Tests if one Task is added correctly to the workspaces list of tasks.
     */
    @Test
    public void testCreateWorkspaceOne() {
        // Arrange
        String name = "Child 1";
        // Act
        t1.createWorkspace(new Task(name));
        // Assert
        assertEquals(1, t1.getTasks().size(), "Ensure that there is only one workspace in task list");
        assertEquals(name, t1.getTasks().get(0).getName(), "Ensure that this workspace has the correct name");
    }
    /**
     * Tests if multiple Tasks and Actions are added to list of tasks successfully.
     */
    @Test
    public void testCreateWorkspaceMultiple() {
        // Arrange
        String name1 = "Child 1";
        String name2 = "Child 2";
        String name3 = "Child 3";
        final int numOfWorkspaces = 3;
        // Act
        t1.createWorkspace(new Task(name1));
        t1.createWorkspace(new Task(name2));
        t1.createWorkspace(new Action(name3));
        // Assert
        assertEquals(numOfWorkspaces, t1.getTasks().size(), "Ensure that there are three workspaces in the task list");
        assertEquals(name1, t1.getTasks().get(0).getName(), "Ensure that first workspace has the correct name");
        assertEquals(name2, t1.getTasks().get(1).getName(), "Ensure that second workspace has the correct name");
        assertEquals(name3, t1.getTasks().get(2).getName(), "Ensure that third workspace has the correct name");
    }
    /**
     * Tests if Tasks and actions can be added in Tasks within the current tasks list of tasks.
     * -- I do not think this is needed
     */
    @Test
    public void testCreateWorkspaceNested() {
        // Arrange
        String name1 = "Child 1";
        String name2 = "Child 2";
        String name3 = "Child 2.1";
        String name4 = "Child 2.2";
        // Act
        t1.createWorkspace(new Task(name1));
        t1.createWorkspace(new Task(name2));
        ((Task) t1.getTasks().get(1)).createWorkspace(new Task(name3));
        ((Task) t1.getTasks().get(1)).createWorkspace(new Action(name4));
        // Assert
        assertEquals(2, t1.getTasks().size(), "Ensure that there are two workspaces in root workspace");
        assertEquals(2, t1.getTasks().get(1).getTasks().size(), "Ensure that there are two workspaces in sub workspace");
        assertEquals(name1, t1.getTasks().get(0).getName(), "Ensure that first workspace has the correct name");
        assertEquals(name2, t1.getTasks().get(1).getName(), "Ensure that second workspace has the correct name");
        assertEquals(name3, t1.getTasks().get(1).getTasks().get(0).getName(), "Ensure that first sub workspace was added correctly");
    }
    /**
     * Tests if Tasks and Actions can be removed from task list.
     */
    @Test
    public void testRemoveWorkspace() {
        // Arrange
        Task temp = new Task("Task 1");
        Action tempAction = new Action("Action 1");
        // Act
        t1.createWorkspace(temp);
        t1.createWorkspace(tempAction);
        t1.deleteWorkspace(temp);
        t1.deleteWorkspace(tempAction);
        // Assert
        assertEquals(0, t1.getTasks().size(), "Workspace should contain no tasks after removing all tasks");
    }
    /**
     * Tests if Tasks importance gets set correctly.
     */
    @Test
    public void testWorkspaceImportance() {
        // Arrange
        // Act
        try {
            t1.setPriority(2);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(2, t1.getPriority(), "Ensure importance for root workspace is set correctly");
    }
    /**
     * Tests if Tasks importance does not get set if invalid number is entered.
     */
    @Test
    public void testWorkspaceImportanceNegative() {
        // Arrange
        final int negValue = -2;
        // Act
        try {
            t1.setPriority(negValue);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(0, t1.getPriority(), "Ensure that importance cannot be set to a negative number");
    }
    /**
     * Tests if Tasks importance does not get set if invalid number is entered.
     */
    @Test
    public void testWorkspaceImportanceTooLarge() {
        // Arrange
        final int largeValue = 50;
        // Act
        try {
            t1.setPriority(largeValue);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(0, t1.getPriority(), "Ensure that importance cannot set to be a number larger than 10");
    }
    /**
     * Tests if Tasks importance does not get set if invalid number is entered.
     */
    @Test
    public void testWorkspaceImportanceRemainSame() {
        // Arrange
        final int priority = 5;
        t1.setPriority(priority);
        final int largeValue = 50;
        // Act
        try {
            t1.setPriority(largeValue);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(priority, t1.getPriority(), "Ensure that importance cannot set to be a number larger than 10");
    }
    /**
     * Tests to see if the due date gets set correctly.
     */
    @Test
    public void testDueDateSet() {
        // Arrange
        final int year = 2020;
        final int month = 4;
        final int day = 6;
        final int hour = 16;
        final int minute = 30;
        final LocalDateTime due = LocalDateTime.of(year, month, day, hour, minute);
        // Act
        t1.setDueDate(year, month, day, hour, minute);
        // Assert
        assertEquals(due, t1.getDueDate(), "Check that due date is set correctly");
    }
    /**
     * Tests if Tasks and Actions get moved correctly.
     */
    @Test
    public void testWorkspaceMove() {
        // Arrange
        final int totalSubWS = 2;
        Task ws1 = new Task("First");
        Task subTask = new Task ("Sub Task");
        Action subAction = new Action ("Sub Action");
        ws1.createWorkspace(subTask);
        ws1.createWorkspace(subAction);
        Task ws2 = new Task("Second");
        t1.createWorkspace(ws1);
        t1.createWorkspace(ws2);
        // Act
        subTask.moveWorkspace(ws2);
        subAction.moveWorkspace(ws2);
        // Assert
        assertEquals(totalSubWS, t1.getTasks().get(1).getTasks().size(), "Ensure workspaces are in second task");
        assertEquals(0, t1.getTasks().get(0).getTasks().size(), "Ensure workspaces are not in original task");
        assertEquals(ws2, subTask.getParent(), "Ensure sub task has correct parent");
        assertEquals(ws2, subAction.getParent(), "Ensure sub action has correct parent");
    }
}
