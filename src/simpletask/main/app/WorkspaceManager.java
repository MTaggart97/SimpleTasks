package simpletask.main.app;

import simpletask.main.entities.Workspace;
import simpletask.main.entities.Task;

/**
 * This class will be responsible for managing the workspace. Through it, you can add
 * to workspace, modify tasks and save workspace.
 */
public class WorkspaceManager {
    /**
     * Users main workspace that the WorkspaceManager manages. Once set, it can never be reset. This
     * is to preven the user from losing their root workspace.
     */
    private final Workspace rootWorkspace;
    /**
     * Current workspace that user is in. This workspace changes regurlarly as the user moves between
     * workspaces.
     */
    private Workspace currentWorkspace = null;
    /**
     * Constructor that takes the name of the workspace to create. It then creates a
     * task with the same name. This task becomes the rootWorkspace and current workspace.
     *
     * @param name  Name of workspace to create
     */
    public WorkspaceManager(final String name) {
        rootWorkspace = new Task(name);
        currentWorkspace = rootWorkspace;
    };

    /**
     * Given a path to a file containing a valid workspace, it will load it in.
     *
     * @param path  Path to workspace
     * @return      True if workspace loads successfully, false otherwise
     */
    public boolean loadWorkspace(final String path) {

        return true;
    }

    /**
     * Adds a workspace into the currentWorkspaces task list if it is a Task.
     *
     * @param newWorkspace  New Workspace to add
     * @return              True if workspace added successfully, false otherwise
     */
    public boolean addWorkspace(final Workspace newWorkspace) {
        if (currentWorkspace instanceof Task) {
            ((Task) currentWorkspace).createWorkspace(newWorkspace);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Given a workspace, it will set the currentWorkspace to it if it is in the currentWorkspace's
     * list of workspaces. Returns new currentWorkspace.
     *
     * @param workspace Workspace to move into
     * @return          The workspace the user moved into
     */
    public Workspace moveIntoWorkspace(final Workspace workspace) {
        boolean found = currentWorkspace.searchWorkspaces(workspace);
        if (found) {
            currentWorkspace = workspace;
            return currentWorkspace;
        } else {
            return currentWorkspace;
        }
    }

    /**
     * Moves currentWorkspace back to root workspace.
     */
    public void moveHome() {
        currentWorkspace = rootWorkspace;
    }

    /**
     * Deletes the currentWorkspace and all its sub Workspaces if any.
     *
     * @return  True if workspace is removed, false otherwise.
     */
    public boolean deleteCurrentWorkspace() {
        return currentWorkspace.delete();
    }

    /**
     * Deletes the workspace in the currentWorkspaces list of workspaces. If the currentWorkspace
     * is not a Task, nothing happens and false is returned.
     *
     * @param workspace Workspace to remove
     * @return          True if workspace is removed, false if not or if currentWorkspace is an Action
     */
    public boolean deleteWorkspace(final Workspace workspace) {
        if (currentWorkspace instanceof Task) {
            return ((Task) currentWorkspace).removeWorkspace(workspace);
        } else {
            return false;
        }
    }
}
