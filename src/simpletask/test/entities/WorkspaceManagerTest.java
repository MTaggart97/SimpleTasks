package simpletask.test.entities;

import simpletask.main.entities.WorkspaceManager;
import simpletask.main.entities.Criteria;
import simpletask.main.entities.InvalidPriorityException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class to test the behaviour of a Test instance.
 */
public class WorkspaceManagerTest {
    /**
     * Used for creating Task objects.
     */
    private static String task = "Task";
    /**
     * Used for creating Action objects.
     */
    private static String action = "Action";
    /**
     * Base task instance to test.
     */
    private WorkspaceManager wm;
    /**
     * Executes before each test method. Simply resets the wm Task instance
     * to be empty.
     */
    @BeforeEach
    public void setUp() {
        wm = WorkspaceManager.initialise("Workspace");
    }
    /**
     * Executed after each tests. Sets the wm Task instance to null to ensure
     * no data is carried over to another test.
     */
    @AfterEach
    public void tearDown() {
        wm = null;
    }
    /**
     * Tests to see if an empty workspace has zero tasks associated with it.
     */
    @Test
    public void testaddWorkspaceZero() {
        // Arrange
        // Act
        // Assert
        assertEquals(0, wm.getTasks().size(), "Workspace should contain no tasks on initialisation");
    }
    /**
     * Tests if one Task is added correctly to the workspaces list of tasks.
     */
    @Test
    public void testaddWorkspaceOne() {
        // Arrange
        String name = "Child 1";
        // Act
        wm.addWorkspace(name, task);
        // Assert
        assertEquals(1, wm.getTasks().size(), "Ensure that there is only one workspace in task list");
        assertEquals(name, wm.getTasks().get(0).get("Name"), "Ensure that this workspace has the correct name");
    }
    /**
     * Tests if multiple Tasks and Actions are added to list of tasks successfully.
     */
    @Test
    public void testaddWorkspaceMultiple() {
        // Arrange
        String name1 = "Child 1";
        String name2 = "Child 2";
        String name3 = "Child 3";
        final int numOfWorkspaces = 3;
        // Act
        wm.addWorkspace(name1, task);
        wm.addWorkspace(name2, task);
        wm.addWorkspace(name3, task);
        // Assert
        assertEquals(numOfWorkspaces, wm.getTasks().size(), "Ensure that there are three workspaces in the task list");
        assertEquals(name1, wm.getTasks().get(0).get("Name"), "Ensure that first workspace has the correct name");
        assertEquals(name2, wm.getTasks().get(1).get("Name"), "Ensure that second workspace has the correct name");
        assertEquals(name3, wm.getTasks().get(2).get("Name"), "Ensure that third workspace has the correct name");
    }
    /**
     * Tests if tasks are added when passed in as dictionaries
     */
    @Test
    public void testaddWorkspaceDict() {
        // Arrange
        Map<String, String> first = new HashMap<>();
        Map<String, String> second = new HashMap<>();
        Map<String, String> third = new HashMap<>();
        first.put("Name", "first");
        second.put("Name", "second");second.put("Type", action);
        third.put("Name", "third");
        // Act
        wm.addWorkspace(first);
        wm.addWorkspace(second);
        wm.addWorkspace(third);
        // Assert
        assertEquals("first", wm.getTasks().get(0).get("Name"), "Ensure that first workspace has the correct name");
        assertEquals("second", wm.getTasks().get(1).get("Name"), "Ensure that second workspace has the correct name");
        assertEquals(action, wm.getTasks().get(1).get("Type"), "Ensure that second workspace has the correct type");
        assertEquals("third", wm.getTasks().get(2).get("Name"), "Ensure that third workspace has the correct name");
    }
    /**
     * Tests if Tasks and actions can be added in Tasks within the current tasks list of tasks.
     * -- I do not think this is needed
     */
    @Test
    public void testaddWorkspaceNested() {
        // Arrange
        String name1 = "Child 1";
        String name2 = "Child 2";
        String name3 = "Child 2.1";
        String name4 = "Child 2.2";
        // Act
        wm.addWorkspace(name1, task);
        wm.addWorkspace(name2, task);
        wm.stepIntoWorkspace(1);
        wm.addWorkspace(name3, action);
        wm.addWorkspace(name4, action);
        wm.home();
        // Assert
        assertEquals(2, wm.getTasks().size(), "Ensure that there are two workspaces in root workspace");
        assertEquals(2, Integer.parseInt(wm.getTasks().get(1).get("Tasks")), "Ensure that there are two workspaces in sub workspace");
        assertEquals(name1, wm.getTasks().get(0).get("Name"), "Ensure that first workspace has the correct name");
        assertEquals(name2, wm.getTasks().get(1).get("Name"), "Ensure that second workspace has the correct name");
        wm.stepIntoWorkspace(1);
        assertEquals(name3, wm.getTasks().get(0).get("Name"), "Ensure that first sub workspace was added correctly");
    }
    /**
     * Tests if Tasks and Actions can be removed from task list.
     */
    @Test
    public void testdeleteWorkspace() {
        // Arrange
        String temp = "Task 1";
        String tempAction = "Action 1";
        // Act
        wm.addWorkspace(temp, task);
        wm.addWorkspace(tempAction, action);
        wm.deleteWorkspace(1);
        wm.deleteWorkspace(0);
        // Assert
        assertEquals(0, wm.getTasks().size(), "Workspace should contain no tasks after removing all tasks");
    }
    /**
     * Tests that delete works for Task object.
     */
    @Test
    public void testDeleteCurrentWorkspace() {
        // Arrange
        String t2 = "Task 1";
        String t3 = "Sub Task";
        String a1 = "Action";
        String a2 = "Sub Action";
        wm.addWorkspace(t2, task);
        wm.addWorkspace(a1, action);
        wm.stepIntoWorkspace(0);
        wm.addWorkspace(t3, task);
        wm.addWorkspace(a2, action);
        wm.home();
        // Act
        wm.deleteCurrentWorkspace();
        // Assert
        assertEquals(0, wm.getTasks().size(), "Ensure that there are no tasks left in current workspace");
        // assertNull(t3.getParent());
        // assertNull(a1.getParent());
        // assertNull(a2.getParent());
    }
    /**
     * Tests if Tasks importance gets set correctly.
     */
    @Test
    public void testWorkspaceImportance() {
        // Arrange
        ArrayList<Integer> path = new ArrayList<>();
        // Act
        try {
            wm.setPriority(2);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(2, Integer.parseInt(wm.detailsOf(path).get("Priority")), "Ensure importance for root workspace is set correctly");
    }
    /**
     * Tests if Tasks importance does not get set if invalid number is entered.
     */
    @Test
    public void testWorkspaceImportanceNegative() {
        // Arrange
        final int negValue = -2;
        ArrayList<Integer> path = new ArrayList<>(0);
        // Act
        try {
            wm.setPriority(negValue);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(0, Integer.parseInt(wm.detailsOf(path).get("Priority")), "Ensure that importance cannot be set to a negative number");
    }
    /**
     * Tests if Tasks importance does not get set if invalid number is entered.
     */
    @Test
    public void testWorkspaceImportanceTooLarge() {
        // Arrange
        final int largeValue = 50;
        ArrayList<Integer> path = new ArrayList<>(0);
        // Act
        try {
            wm.setPriority(largeValue);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(0, Integer.parseInt(wm.detailsOf(path).get("Priority")), "Ensure that importance cannot set to be a number larger than 10");
    }
    /**
     * Tests if Tasks importance does not get set if invalid number is entered.
     */
    @Test
    public void testWorkspaceImportanceRemainSame() {
        // Arrange
        final int priority = 5;
        wm.setPriority(priority);
        final int largeValue = 50;
        ArrayList<Integer> path = new ArrayList<>(0);
        // Act
        try {
            wm.setPriority(largeValue);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(priority, Integer.parseInt(wm.detailsOf(path).get("Priority")), "Ensure that importance cannot set to be a number larger than 10");
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
        ArrayList<Integer> path = new ArrayList<>();
        // Act
        wm.setDueDate(year, month, day, hour, minute);
        // Assert
        assertEquals(due.toString(), wm.detailsOf(path).get("DueDate"), "Check that due date is set correctly");
    }
    /**
     * Tests if Tasks and Actions get moved correctly.
     */
    @Test
    public void testWorkspaceMove() {
        // Arrange
        final int totalSubWS = 2;
        String ws1 = "First";
        wm.addWorkspace(ws1, task);
        wm.stepIntoWorkspace(0);
        String subTask = "Sub Task";
        String subAction = "Sub Action";
        wm.addWorkspace(subTask, task);
        wm.addWorkspace(subAction, action);
        wm.home();
        String ws2 = "Second";
        wm.addWorkspace(ws2, task);

        ArrayList<Integer> path = new ArrayList<>();
        path.add(1);
        // Act
        wm.stepIntoWorkspace(0);
        wm.stepIntoWorkspace(0);
        wm.moveCurrentWorkspace(new ArrayList<Integer>(path));
        wm.home();
        wm.stepIntoWorkspace(0);
        wm.stepIntoWorkspace(0);
        wm.moveCurrentWorkspace(new ArrayList<Integer>(path));
        wm.home();
        // Assert
        assertEquals(totalSubWS, Integer.parseInt(wm.getTasks().get(1).get("Tasks")), "Ensure workspaces are in second task");
        assertEquals(0, Integer.parseInt(wm.getTasks().get(0).get("Tasks")), "Ensure workspaces are not in original task");
    }
    /**
     * Tests to see if workspace search for Task objects works.
     */
    @Test
    public void testTaskSearch() {
        // Arrange
        String t2 = "Task 1";
        String t3 = "Task 2";
        String a1 = "Action 1";
        String a2 = "Action 2";

        Criteria c2 = new Criteria().addName(t2);
        Criteria ca1 = new Criteria().addName(a1);

        wm.addWorkspace(t2, task);
        wm.addWorkspace(t3, task);
        wm.stepIntoWorkspace(0);
        wm.addWorkspace(a1, action);
        wm.home();
        wm.addWorkspace(a2, action);
        // Act
        ArrayList<Map<String, String>> foundT2 = wm.searchWorkspaces(c2);
        ArrayList<Map<String, String>> foundA2 = wm.searchWorkspaces(ca1);
        ArrayList<Map<String, String>> randomTask = wm.searchWorkspaces(new Criteria().addName("Random"));
        // Assert
        assertEquals(1, foundT2.size(), "Look for Task in list of workspaces");
        assertEquals(t2, foundT2.get(0).get("Name"), "Ensure task has the correct name");
        assertEquals(1, foundA2.size(), "Look for Action in list of workspaces");
        assertEquals(a1, foundA2.get(0).get("Name"), "Ensure action has the correct name");
        assertEquals(0, randomTask.size(), "Look for Task that is not in list of workspaces");
    }

    /**
     * Tests to see if workspace search for Task and Action objects works when multiple nodes are found.
     */
    @Test
    public void testSearchMultiReturn() {
        // Arrange
        String t2 = "Task 1";
        String t3 = "Task 2";
        String a2 = "Action 2";

        Criteria c2 = new Criteria().addName(t2);
        Criteria ca1 = new Criteria().addName(t2).addType(action);

        wm.addWorkspace(t2, task);
        wm.addWorkspace(t3, task);
        wm.stepIntoWorkspace(0);
        wm.addWorkspace(t2, action);
        wm.home();
        wm.addWorkspace(a2, action);
        // Act
        ArrayList<Map<String, String>> foundT2 = wm.searchWorkspaces(c2);
        ArrayList<Map<String, String>> foundA2 = wm.searchWorkspaces(ca1);
        // Assert
        assertEquals(2, foundT2.size(), "Should find two nodes with name: " + t2);
        assertEquals(t2, foundT2.get(0).get("Name"), "Ensure task has the correct name");
        assertEquals(t2, foundT2.get(1).get("Name"), "Ensure Action has the correct name");
        assertEquals(1, foundA2.size(), "Look for Action in list of workspaces");
        assertEquals(action, foundA2.get(0).get("Type"), "Ensure action has the correct type");
    }
}
