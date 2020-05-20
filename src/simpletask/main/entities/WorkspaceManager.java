package simpletask.main.entities;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class will be responsible for managing the workspace. Through it, you can add
 * to workspace, modify tasks and save workspace.
 */
public class WorkspaceManager {
    /**
     * Users main workspace that the WorkspaceManager manages. Once set, it can never be reset. This
     * is to preven the user from losing their root workspace.
     */
    private final Task rootWorkspace;
    /**
     * Current workspace that user is in. This workspace changes regurlarly as the user moves between
     * workspaces.
     */
    private Task currentWorkspace = null;
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
     * Returns a map of details of the currentWorkspace's parent. Keys include:
     *  Name    :   Name of Parent
     *  Priority:   Prioity of Task
     *  Type    :   Will always be "Task" as a parent cannot be an Action
     *  Tasks   :   Number of tasks in parents list
     *
     * @return  Current workspace's parent
     */
    public Map<String, String> getParent() {
        Task parent = currentWorkspace.getParent();
        return parent.getDetails();
    }
    /**
     * Returns a list of summary details about the current workspaces tasks.
     *
     * @return  Details on the current Workspaces tasks
     */
    public ArrayList<Map<String, String>> getTasks() {
        ArrayList<Map<String, String>> array = new ArrayList<Map<String, String>>();

        for (Task w: currentWorkspace.getTasks()) {
            array.add(w.getDetails());
        }

        return array;
    }
    /**
     * Returns details of the Workspace at the given path.
     *
     * @param   path    Path to workspace
     * @return          Dictionary containing details of workspace at path
     */
    public Map<String, String> detailsOf(final ArrayList<Integer> path) {
        Task w = rootWorkspace;
        for (Integer i: path) {
            w = w.getTasks().get(i);
        }
        return w.getDetails();
    }
    /**
     * Returns a list of details for the Task at the given path.
     *
     * @param   path    Path to workspace
     * @return          List of dictionaries containing details of workspace at path
     */
    public ArrayList<Map<String, String>> taskDetailsOf(final ArrayList<Integer> path) {
        Task w = rootWorkspace;
        for (Integer i: path) {
            w = w.getTasks().get(i);
        }

        ArrayList<Map<String, String>> array = new ArrayList<Map<String, String>>();
        for (Task wrk: w.getTasks()) {
            array.add(wrk.getDetails());
        }

        return array;
    }
    /**
     * Given a path to a file containing a valid workspace, it will load it in. That workspace
     * will become the rootWorkspace.
     *
     * @param path  Path to workspace
     * @return      True if workspace loads successfully, false otherwise
     */
    public boolean loadWorkspace(final String path) {

        return true;
    }
    /**
     * Given a path to a valid location, it will save the rootWorkspace to that location.
     *
     * @param   path    Path to save rootWorkspace to.
     * @return          True if workspace saved successfully.
     */
    public boolean save(final String path) {

        return true;
    }
    /**
     * Adds a workspace into the currentWorkspaces task list if it is a Task.
     *
     * @param newWorkspace  New Workspace to add
     * @return              True if workspace added successfully, false otherwise
     */
    public boolean addWorkspace(final Task newWorkspace) {
        if (currentWorkspace instanceof Task) {
            ((Task) currentWorkspace).createWorkspace(newWorkspace);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Given an integer, this will move the current workspace into that position in its
     * list of tasks. If the position doesn't exist, then nothing happens.
     *
     * @param pos       Position in subTasks of Workspace to move into
     * @return          The workspace the user moved into
     */
    public Task stepIntoWorkspace(final int pos) {
        try {
            Task workspace = currentWorkspace.getTasks().get(pos);
            currentWorkspace = workspace;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Doing nothing");
        }
        return currentWorkspace;
    }
    /**
     * Moves currentWorkspace back to root workspace.
     */
    public void home() {
        currentWorkspace = rootWorkspace;
    }
    /**
     * Moves currentWorkspace up one.
     */
    public void stepUp() {
        currentWorkspace = currentWorkspace.getParent();
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
     * Deletes the workspace in the currentWorkspaces list of workspaces at position pos. If the currentWorkspace
     * is not a Task, nothing happens and false is returned. Fails if index entered is out of bounds.
     *
     * @param pos       Position of workspace to remove
     * @return          True if workspace is removed, false if not or if currentWorkspace is an Action
     */
    public boolean deleteWorkspace(final int pos) {
        try {
            Task workspace = currentWorkspace.getTasks().get(pos);
            if (!(currentWorkspace instanceof Task)) {
                return false;
            }
            return ((Task) currentWorkspace).removeWorkspace(workspace);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    /**
     * Moves currentWorkspace into target if the target is a Task and if the target
     * exists under the current rootWorkspace. The target is got from the inputted path
     * which is directions to the task from the rootWorkspace
     *
     * @param   path    Path to Task
     * @return          True if workspace is move successfully
     */
    public boolean moveCurrentWorkspace(final ArrayList<Integer> path) {
        Task target = rootWorkspace;
        for (Integer i: path) {
            target = target.getTasks().get(i);
        }
        // Cannot move currentWorkspace if target is not a Task or if it doesn't exist
        if (!(target instanceof Task) || !rootWorkspace.searchWorkspaces(target)) {
            return false;
        }

        return currentWorkspace.moveWorkspace((Task) target);
    }
    /**
     * Sets name of currentWorkspace.
     *
     * @param name  Name of workspace.
     */
    public void setName(final String name) {
        currentWorkspace.setName(name);
    }
    /**
     * Set description of currentWorkspace.
     *
     * @param   msg Description of workspace
     */
    public void setDescription(final String msg) {
        currentWorkspace.setDescription(msg);
    }
    /**
     * Set priority of currentWorkspace.
     *
     * @param priority  New priority of workspace
     * @return          True if priority set successfully, false otherwise
     */
    public boolean setPrioirty(final int priority) {
        try {
            currentWorkspace.setPriority(priority);
            return true;
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Creates a string representing the  currentWorkspace. If current workspace is a Task, it
     * will first add all its sub tasks.
     *
     * @return  Message to display
     */
    private String display() {
        StringBuilder msg = new StringBuilder(currentWorkspace.getName() + "\n");
        for (Task w: currentWorkspace.getTasks()) {
            msg.append(w.toString());
        }
        return msg.toString();
    }
    /**
     * Override of default Object toString method. Redirects to display.
     *
     * @return  Message to display when printing this object.
     */
    @Override
    public String toString() {
        return display();
    }
}
