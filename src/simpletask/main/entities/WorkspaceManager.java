package simpletask.main.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will be responsible for managing the workspace. Through it, you can add
 * to workspace, modify tasks and save workspace.
 */
public class WorkspaceManager {
    //#region [Fields]
    /**
     * Users main workspace that the WorkspaceManager manages. Once set, it can never be reset. This
     * is to preven the user from losing their root workspace.
     */
    private final WorkspaceNode rootWorkspace;
    /**
     * Current workspace that user is in. This workspace changes regurlarly as the user moves between
     * workspaces.
     */
    private WorkspaceNode currentWorkspace = null;
    //#endregion [Fields]

    //#region [Constructors]
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
    //#endregion [Constructors]

    //#region [Getters]
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
        Task parent = (Task) currentWorkspace.getParent();
        return getDetails(parent);
    }
    /**
     * Returns a list of summary details about the current workspaces tasks.
     *
     * @return  Details on the current Workspaces tasks
     */
    public ArrayList<Map<String, String>> getTasks() {
        ArrayList<Map<String, String>> array = new ArrayList<Map<String, String>>();

        for (WorkspaceNode w: currentWorkspace.getTasks()) {
            array.add(getDetails(w));
        }

        return array;
    }
    /**
     * Given a task, it will return a summary of it. This is used as a helper function for 
     * other methods in this class that return info about Tasks without returning the instance
     * itself.
     *
     * @param task  Task to summarise
     * @return      A dictionary
     */
    private Map<String, String> getDetails(final WorkspaceNode task) {
        Map<String, String> dict = new HashMap<String, String>();

        dict.put("Name", task.getName());
        dict.put("Priority", String.valueOf(task.getPriority()));
        dict.put("Type", task.getClass().getSimpleName());
        dict.put("Tasks", String.valueOf(task.getTasks().size()));
        dict.put("DueDate",task.getDueDate().toString());

        return dict;
    }
    /**
     * Returns details of the Workspace at the given path.
     *
     * @param   path    Path to workspace
     * @return          Dictionary containing details of workspace at path
     */
    public Map<String, String> detailsOf(final ArrayList<Integer> path) {
        WorkspaceNode w = rootWorkspace;
        for (Integer i: path) {
            w = w.getTasks().get(i);
        }
        return getDetails(w);
    }
    /**
     * Returns a list of details for the Task at the given path.
     *
     * @param   path    Path to workspace
     * @return          List of dictionaries containing details of workspace at path
     */
    public ArrayList<Map<String, String>> taskDetailsOf(final ArrayList<Integer> path) {
        WorkspaceNode w = rootWorkspace;
        for (Integer i: path) {
            w = w.getTasks().get(i);
        }

        ArrayList<Map<String, String>> array = new ArrayList<Map<String, String>>();
        for (WorkspaceNode wrk: w.getTasks()) {
            array.add(getDetails(wrk));
        }

        return array;
    }
    //#endregion [Getters]

    //#region [Load/Save]
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
    //#endregion [Load/Save]

    //#region [Movement]
    /**
     * Given an integer, this will move the current workspace into that position in its
     * list of tasks. If the position doesn't exist, then nothing happens.
     *
     * @param pos       Position in subTasks of Workspace to move into
     * @return          The workspace the user moved into
     */
    public WorkspaceNode stepIntoWorkspace(final int pos) {
        try {
            WorkspaceNode workspace = currentWorkspace.getTasks().get(pos);
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
    //#endregion [Movement]

    //#region [Workspace Management]
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
            WorkspaceNode workspace = currentWorkspace.getTasks().get(pos);
            if (!(currentWorkspace instanceof Task)) {
                return false;
            }
            return ((Task) currentWorkspace).removeWorkspace(workspace);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
        /**
     * Adds a workspace into the currentWorkspaces task list if it is a Task.
     *
     * @param newWorkspace  Name of Workspace to add
     * @param type          Type of Workspace to add
     * @return              True if workspace added successfully, false otherwise
     */
    public boolean addWorkspace(final String name, final String type) {
        WorkspaceNode newWorkspace;
        switch (type) {
            case ("Action"):
                newWorkspace = new Action(name);
                break;
            case ("Task"):
                newWorkspace = new Task(name);
                break;
            default:
                return false;
        }
        if (currentWorkspace instanceof Task) {
            ((Task) currentWorkspace).createWorkspace(newWorkspace);
            return true;
        } else {
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
        WorkspaceNode target = rootWorkspace;
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
     * Used to search for tasks in the currentWorkspace given a search Criteria.
     *
     * @param name  The search Criteria
     * @return      A list of Tasks matching the criteria
     */
    public ArrayList<Map<String,String>> searchWorkspaces(final Criteria criteria) {
        ArrayList<Map<String,String>> res = new ArrayList<>();
        for(int i = 0; i <  currentWorkspace.getTasks().size(); i++) {
            this.stepIntoWorkspace(i);
            res.addAll(this.searchWorkspaces(criteria));
            this.stepUp();
            if (criteria.compare(this.getTasks().get(i))) {
                res.add(this.getTasks().get(i));
            }
        }
        return res;
    }
    //#endregion [Workspace Management]
    
    //#region [Setters]
    /**
     * Sets name of currentWorkspace.
     *
     * @param name  Name of workspace.
     */
    public void setName(final String name) {
        currentWorkspace.setName(name);
    }
    /**
     * Sets the dueDate for the currentWorkspace.
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     */
    public void setDueDate(final int year, final int month, final int day, final int hour, final int minute) {
        currentWorkspace.setDueDate(year, month, day, hour, minute);
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
    public boolean setPriority(final int priority) {
        try {
            currentWorkspace.setPriority(priority);
            return true;
        } catch (InvalidPriorityException e) {
            e.printStackTrace();
            return false;
        }
    }
    //#endregion [Setters]

    /**
     * Creates a string representing the  currentWorkspace. If current workspace is a Task, it
     * will first add all its sub tasks.
     *
     * @return  Message to display
     */
    private String display() {
        StringBuilder msg = new StringBuilder(currentWorkspace.getName() + "\n");
        for (WorkspaceNode w: currentWorkspace.getTasks()) {
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
