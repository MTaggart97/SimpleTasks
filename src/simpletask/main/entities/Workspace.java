package simpletask.main.entities;

import java.io.Serializable;
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
public interface Workspace extends Serializable {
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
     * Returns the parent of the current workspace.
     *
     * @return  Parent of current workspace
     */
    Workspace getParent();
    /**
     * Moves the current workspace into another Tasks list.
     *
     * @param   target  The target/parent task
     * @return          True if successful, fasle otherwise
     */
    boolean moveWorkspace(Task target);
    /**
     * Searches the Workspaces list of Workspaces for object provided.
     *
     * @param workspace Workspace to find.
     * @return          True if workspace is found, false otherwise.
     */
    boolean searchWorkspaces(Workspace workspace);
    /**
     * Responisble for deleting the current Workspace. Each workspace is
     * responisble for removing any resources that it is using.
     *
     * @return  True if workspace is delete successfully, false otherwise.
     */
    boolean delete();
}
