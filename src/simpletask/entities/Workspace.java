package simpletask.entities;

import java.util.ArrayList;

/**
 * An interface used by all task classes. A workspace is anywhere that stores
 * data for tasks. This may just be a list of tasks or a simple action.
 *
 * NOTE:
 * At the moment, all methods have default privilages but some will need to be
 * changed to public.
 *
 * @author Matthew Taggart
 * @see Task
 * @see Action
 */
public interface Workspace {
    /**
     * Used to check if current workspace is complete.
     *
     * @return True if workspace is complete, fasle otherwise
     */
    boolean isWorkspaceComplete();
    /**
     * Used to flip the completion status of the workspace.
     *
     * @return True if workspace is complete, false otherwise.
     */
    boolean flipCompletionStatus();
    /**
     * Used to create a new workspace.
     *
     * @param name  Name of new workspace
     * @return The newly created workspace
     */
    Workspace createWorkspace(String name);
    /**
     * Used to get name of workspace.
     *
     * @return Name of workspace
     */
    String getName();
    /**
     * Used to get a list of tasks in current workspace.
     *
     * @return An array list of tasks in workspaces
     */
    ArrayList<Workspace> getTasks();
    /**
     * Used to set the parent of the current workspace. As of now,
     * the parent of a workspace, is the workspace it was created in.
     *
     * @param parent    The parent of this workspace.
     */
    void setParent(Workspace parent);
    /**
     * Returns the parent of the current workspace.
     *
     * @return  Parent of current workspace
     */
    Workspace getParent();

    //TODO Implement a save feature so that you can save individual
    //TODO  items without overwriting the whole workspace#
    // boolean save();
    // Recursively save itself and all sub-tasks

    //TODO Implement a move workspace api used to move on workspace
    //TODO to another, i.e. update its parents
}
