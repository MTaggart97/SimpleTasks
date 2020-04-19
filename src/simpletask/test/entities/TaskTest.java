package simpletask.test.entities;

import simpletask.main.entities.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTest {
    private Task t1;

    @BeforeEach
    public void setUp() {
        t1 = new Task("Workspace");
    } 

    @AfterEach
    public void tearDown() {
        t1 = null;
    }

    @Test
    public void testCreateWorkspace_Zero() {
        // Arrange
        
        // Act
        
        // Assert
        assertEquals(0, t1.getTasks().size(), "Workspace should contain no tasks on initialisation");
    }
    @Test
    public void testCreateWorkspace_One() {
        // Arrange
        String name = "Child 1";
        // Act
        t1.createWorkspace(name);
        // Assert
        assertEquals(1, t1.getTasks().size(), "Ensure that there is only one workspace in task list");
        assertEquals(name, t1.getTasks().get(0).getName(), "Ensure that this workspace has the correct name");
    }

    @Test
    public void testCreateWorkspace_Multiple() {
        // Arrange
        String name1 = "Child 1";
        String name2 = "Child 2";
        String name3 = "Child 3";
        // Act
        t1.createWorkspace(name1);
        t1.createWorkspace(name2);
        t1.createWorkspace(name3);
        // Assert
        assertEquals(3, t1.getTasks().size(), "Ensure that there are three workspaces in the task list");
        assertEquals(name1, t1.getTasks().get(0).getName(), "Ensure that first workspace has the correct name");
        assertEquals(name2, t1.getTasks().get(1).getName(), "Ensure that second workspace has the correct name");
        assertEquals(name3, t1.getTasks().get(2).getName(), "Ensure that third workspace has the correct name");
    }

    @Test
    public void testCreateWorkspace_Nested() {
        // Arrange
        String name1 = "Child 1";
        String name2 = "Child 2";
        String name3 = "Child 2.1";
        String name4 = "Child 2.2";
        // Act
        t1.createWorkspace(name1);
        t1.createWorkspace(name2);
        t1.getTasks().get(1).createWorkspace(name3);
        t1.getTasks().get(1).createWorkspace(name4);
        // Assert
        assertEquals(2, t1.getTasks().size(), "Ensure that there are two workspaces in root workspace");
        assertEquals(2, t1.getTasks().get(1).getTasks().size(), "Ensure that there are two workspaces in sub workspace");
        assertEquals(name1, t1.getTasks().get(0).getName(), "Ensure that first workspace has the correct name");
        assertEquals(name2, t1.getTasks().get(1).getName(), "Ensure that second workspace has the correct name");
        assertEquals(name3, t1.getTasks().get(1).getTasks().get(0).getName(), "Ensure that first sub workspace was added correctly");
    }
}
