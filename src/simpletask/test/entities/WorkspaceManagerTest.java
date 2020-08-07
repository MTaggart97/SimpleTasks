package simpletask.test.entities;

import simpletask.main.entities.WorkspaceManager;
import simpletask.main.entities.Criteria;
import simpletask.main.entities.InvalidPriorityException;
import simpletask.main.entities.NodeData;
import simpletask.main.entities.NodeKeys;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
     * Name of base workspace.
     */
    private String workspaceName = "Workspace";
    /**
     * Executes before each test method. Simply resets the wm Task instance
     * to be empty.
     */
    @BeforeEach
    public void setUp() {
        wm = WorkspaceManager.initialise(workspaceName);
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
        assertEquals(name, wm.getTasks().get(0).getAttr(NodeKeys.NAME), "Ensure that this workspace has the correct name");
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
        assertEquals(name1, wm.getTasks().get(0).getAttr(NodeKeys.NAME), "Ensure that first workspace has the correct name");
        assertEquals(name2, wm.getTasks().get(1).getAttr(NodeKeys.NAME), "Ensure that second workspace has the correct name");
        assertEquals(name3, wm.getTasks().get(2).getAttr(NodeKeys.NAME), "Ensure that third workspace has the correct name");
    }
    /**
     * Tests if tasks are added when passed in as dictionaries
     */
    @Test
    public void testaddWorkspaceDict() {
        // Arrange
        NodeData first = new NodeData();
        NodeData second = new NodeData();
        NodeData third = new NodeData();
        first.setAttr(NodeKeys.NAME, "first");
        second.setAttr(NodeKeys.NAME, "second");second.setAttr(NodeKeys.TYPE, action);
        third.setAttr(NodeKeys.NAME, "third");
        // Act
        wm.addWorkspace(first);
        wm.addWorkspace(second);
        wm.addWorkspace(third);
        // Assert
        assertEquals("first", wm.getTasks().get(0).getAttr(NodeKeys.NAME), "Ensure that first workspace has the correct name");
        assertEquals("second", wm.getTasks().get(1).getAttr(NodeKeys.NAME), "Ensure that second workspace has the correct name");
        assertEquals(action, wm.getTasks().get(1).getAttr(NodeKeys.TYPE), "Ensure that second workspace has the correct type");
        assertEquals("third", wm.getTasks().get(2).getAttr(NodeKeys.NAME), "Ensure that third workspace has the correct name");
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
        assertEquals(2, Integer.parseInt(wm.getTasks().get(1).getAttr(NodeKeys.TASKS)), "Ensure that there are two workspaces in sub workspace");
        assertEquals(name1, wm.getTasks().get(0).getAttr(NodeKeys.NAME), "Ensure that first workspace has the correct name");
        assertEquals(name2, wm.getTasks().get(1).getAttr(NodeKeys.NAME), "Ensure that second workspace has the correct name");
        wm.stepIntoWorkspace(1);
        assertEquals(name3, wm.getTasks().get(0).getAttr(NodeKeys.NAME), "Ensure that first sub workspace was added correctly");
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
            wm.setPriority("2");
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(2, Integer.parseInt(wm.detailsOf(path).getAttr(NodeKeys.PRIORITY)), "Ensure importance for root workspace is set correctly");
    }
    /**
     * Tests if Tasks importance does not get set if invalid number is entered.
     */
    @Test
    public void testWorkspaceImportanceNegative() {
        // Arrange
        final String negValue = "-2";
        ArrayList<Integer> path = new ArrayList<>(0);
        // Act
        try {
            wm.setPriority(negValue);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(0, Integer.parseInt(wm.detailsOf(path).getAttr(NodeKeys.PRIORITY)), "Ensure that importance cannot be set to a negative number");
    }
    /**
     * Tests if Tasks importance does not get set if invalid number is entered.
     */
    @Test
    public void testWorkspaceImportanceTooLarge() {
        // Arrange
        final String largeValue = "50";
        ArrayList<Integer> path = new ArrayList<>(0);
        // Act
        try {
            wm.setPriority(largeValue);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(0, Integer.parseInt(wm.detailsOf(path).getAttr(NodeKeys.PRIORITY)), "Ensure that importance cannot set to be a number larger than 10");
    }
    /**
     * Tests if Tasks importance does not get set if invalid number is entered.
     */
    @Test
    public void testWorkspaceImportanceRemainSame() {
        // Arrange
        final String priority = "5";
        wm.setPriority(priority);
        final String largeValue = "50";
        ArrayList<Integer> path = new ArrayList<>(0);
        // Act
        try {
            wm.setPriority(largeValue);
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
        }
        // Assert
        assertEquals(Integer.parseInt(priority), Integer.parseInt(wm.detailsOf(path).getAttr(NodeKeys.PRIORITY)), "Ensure that importance cannot set to be a number larger than 10");
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
        assertEquals(due.toString(), wm.detailsOf(path).getAttr(NodeKeys.DUEDATE), "Check that due date is set correctly");
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
        assertEquals(totalSubWS, Integer.parseInt(wm.getTasks().get(1).getAttr(NodeKeys.TASKS)), "Ensure workspaces are in second task");
        assertEquals(0, Integer.parseInt(wm.getTasks().get(0).getAttr(NodeKeys.TASKS)), "Ensure workspaces are not in original task");
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

        Criteria c2 = new Criteria().addAttr(NodeKeys.NAME, t2);
        Criteria ca1 = new Criteria().addAttr(NodeKeys.NAME, a1);

        wm.addWorkspace(t2, task);
        wm.addWorkspace(t3, task);
        wm.stepIntoWorkspace(0);
        wm.addWorkspace(a1, action);
        wm.home();
        wm.addWorkspace(a2, action);
        // Act
        ArrayList<NodeData> foundT2 = wm.searchWorkspaces(c2);
        ArrayList<NodeData> foundA2 = wm.searchWorkspaces(ca1);
        ArrayList<NodeData> randomTask = wm.searchWorkspaces(new Criteria().addAttr(NodeKeys.NAME, "Random"));
        // Assert
        assertEquals(1, foundT2.size(), "Look for Task in list of workspaces");
        assertEquals(t2, foundT2.get(0).getAttr(NodeKeys.NAME), "Ensure task has the correct name");
        assertEquals(1, foundA2.size(), "Look for Action in list of workspaces");
        assertEquals(a1, foundA2.get(0).getAttr(NodeKeys.NAME), "Ensure action has the correct name");
        assertEquals(0, randomTask.size(), "Look for Task that is not in list of workspaces");
    }

    /**
     * Tests to see if nodes are found when they are deep inside the workspace
     */
    @Test
    public void testSearchRecursive() {
        String t2 = "Task 1";
        String t3 = "Task 2";
        String a1 = "Action 1";
        String a2 = "Action 2";

        Criteria c2 = new Criteria().addAttr(NodeKeys.NAME, t2);
        Criteria ca1 = new Criteria().addAttr(NodeKeys.NAME, a1);

        wm.addWorkspace(t2, task);
        wm.stepIntoWorkspace(0);
        wm.addWorkspace(t3, task);
        wm.stepIntoWorkspace(0);
        wm.addWorkspace(a1, action);
        wm.addWorkspace(a2, action);
        wm.home();
        // Act
        ArrayList<NodeData> foundT2 = wm.searchWorkspaces(c2);
        ArrayList<NodeData> foundA2 = wm.searchWorkspaces(ca1);
        ArrayList<NodeData> randomTask = wm.searchWorkspaces(new Criteria().addAttr(NodeKeys.NAME, "Random"));

        // Assert
        assertEquals(1, foundT2.size(), "Look for Task in list of workspaces");
        assertEquals(t2, foundT2.get(0).getAttr(NodeKeys.NAME), "Ensure task has the correct name");
        assertEquals(1, foundA2.size(), "Look for Action in list of workspaces");
        assertEquals(a1, foundA2.get(0).getAttr(NodeKeys.NAME), "Ensure action has the correct name");
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

        Criteria c2 = new Criteria().addAttr(NodeKeys.NAME,t2);
        Criteria ca1 = new Criteria().addAttr(NodeKeys.NAME,t2).addAttr(NodeKeys.TYPE,action);

        wm.addWorkspace(t2, task);
        wm.addWorkspace(t3, task);
        wm.stepIntoWorkspace(0);
        wm.addWorkspace(t2, action);
        wm.home();
        wm.addWorkspace(a2, action);
        // Act
        ArrayList<NodeData> foundT2 = wm.searchWorkspaces(c2);
        ArrayList<NodeData> foundA2 = wm.searchWorkspaces(ca1);
        // Assert
        assertEquals(2, foundT2.size(), "Should find two nodes with name: " + t2);
        assertEquals(t2, foundT2.get(0).getAttr(NodeKeys.NAME), "Ensure task has the correct name");
        assertEquals(t2, foundT2.get(1).getAttr(NodeKeys.NAME), "Ensure Action has the correct name");
        assertEquals(1, foundA2.size(), "Look for Action in list of workspaces");
        assertEquals(action, foundA2.get(0).getAttr(NodeKeys.TYPE), "Ensure action has the correct type");
    }

    /**
     * Tests getCurrentWorkspaceDetails method on unmodified workspace.
     */
    @Test
    public void testGetCurrentWorkspaceDetails() {
        // Arrange

        // Act
        
        // Assert
        assertEquals(workspaceName, wm.getCurrentWorkspaceDetails().getAttr(NodeKeys.NAME), "Ensure that name of root workspace is " + workspaceName);
        assertEquals("0", wm.getCurrentWorkspaceDetails().getAttr(NodeKeys.TASKS), "Ensure that root workspace has no sub tasks");
    }
    
    /**
     * 
     */
    @Test
    public void testGetCurrentWorkspaceDetailsModified() {
        // Arrange
        final String totalSubWS = "2";
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
        // Act
        wm.stepIntoWorkspace(0);
        String ws1Name = wm.getCurrentWorkspaceDetails().getAttr(NodeKeys.NAME);
        wm.home();
        wm.stepIntoWorkspace(1);
        String ws2Name = wm.getCurrentWorkspaceDetails().getAttr(NodeKeys.NAME);
        wm.home();
        String numOfWS = wm.getCurrentWorkspaceDetails().getAttr(NodeKeys.TASKS);
        // Assert
        assertEquals(totalSubWS, numOfWS, "Ensure that there are " + totalSubWS + " workspaces in root workspace");
        assertEquals(ws1Name, ws1, "Ensure first workspace has correct name");
        assertEquals(ws2Name, ws2, "Ensure second workspace has correct name");
    }

    /**
     * Tests to see if relative details works correctly
     */
    @Test
    public void testRelativeDetailsOf() {
        // Arrange
        String t2 = "Task 1";
        String t3 = "Task 2";
        String a2 = "Action 2";

        wm.addWorkspace(t2, task);
        wm.stepIntoWorkspace(0);
        wm.addWorkspace(t3, task);
        wm.stepIntoWorkspace(0);
        wm.addWorkspace(a2, action);
        wm.home();

        ArrayList<Integer> path = new ArrayList<>();
        path.add(0);path.add(0);
        // Act
        wm.stepIntoWorkspace(0);
        NodeData data = wm.relativeDetailsOf(path);

        // Assert
        assertEquals("Action 2", data.getAttr(NodeKeys.NAME), "Ensure that the correct task is returned from relative details");
    }
}
