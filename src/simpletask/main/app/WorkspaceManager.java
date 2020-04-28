package simpletask.main.app;

import simpletask.main.entities.Workspace;
import simpletask.main.entities.Task;

import java.util.Scanner;

import simpletask.main.entities.Action;

/**
 * This class will be responsible for managing the workspace. Through it, you can add
 * to workspace, modify tasks and save workspace.
 */
public class WorkspaceManager {
    /**
     * Users main workspace that the WorkspaceManager manages.
     */
    private Workspace workspace = null;
    /**
     * Scanner object used for input. Default to read from stdin.
     */
    private Scanner sc = new Scanner(System.in);
    /**
     * Default constructor. Currently does nothing.
     */
    public WorkspaceManager() {

    };

    public void createWorkspace(final String name) {
        workspace = new Task(name);
    }
}
