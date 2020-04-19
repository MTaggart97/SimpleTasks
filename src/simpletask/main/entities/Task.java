package simpletask.main.entities;

import java.util.ArrayList;

/**
 * A task is a workspace that can contain other workspaces within
 * itself. It will be the main way in which to manage tasks/actions.
 * A task could contian multiple workspaces, i.e. a todo workspace,
 * an in progress workspace, an action etc. This makes a task very
 * flexible and allows the user to create a workspace that suits them
 *
 * @author Matthew Taggart
 * @see Workspace
 */
public class Task implements Workspace {
    // Instance Variables
    /**
     * Name of task.
     */
    private String name;
    /**
     * Description of task.
     */
    private String description;
    //TODO Look up best way to implement date
    // private Date dueDate;
    /**
     * Flag to mark if task is complete.
     */
    private boolean complete;
    /**
     * Importance of task. From 1-10. Using byte to minimise storage
     */
    private byte importance;
    /**
     * A list of tasks maintianed by current task. For a task to be
     * marked as complete, all its tasks must be marked as complete
     */
    private ArrayList<Workspace> tasks = new ArrayList<Workspace>();
    /**
     * Parent of current task. This is generally used to keep track of
     * the current task, i.e. todo, in progress, Assignment 1 etc.
     */
    private Workspace parent = null;

    // Class constants
    /**
     * String that is appended to the task when it is being printed
     * to the console. Not functional, just for neatness.
     */
    private static final String DISPLAYHEADER = "\n-------------------------------------\n";

    /**
     * Private constructor, don't know if needed yet.
     */
    private Task() {

    }

    /**
     * The basic constructor for a task. At most, a task needs a name.
     * By default, a task is it's own parent.
     *
     * @param name  The name of the task
     */
    public Task(final String name) {
        this.name = name;
        parent = this;
    }

    /**
     * Returns name of the task.
     *
     *  @return Name of task as string
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns a list of all workspaces contained in the current task.
     *
     *  @return All workspaces tracked by current task
     */
    @Override
    public ArrayList<Workspace> getTasks() {
        return tasks;
    }

    /**
     * Sets the parent of the current task to the inputted workspace.
     *
     *  @param parent    The parent workspace
     */
    @Override
    public void setParent(final Workspace parent) {
        this.parent = parent;
    }

    /**
     * Returns parent of current task.
     *
     *  @return The current tasks parent
     */
    @Override
    public Workspace getParent() {
        return this.parent;
    }

    /**
     * Returns completion status of current task.
     *
     * @return True if complete, false otherwise
     */
    @Override
    public boolean isWorkspaceComplete() {
        return complete;
    }

    /**
     * Flips the completion status of the current workspace. Can be used
     * by the user if they wish to manually change the completion status
     * of the current workspace.
     *
     * @return The completion status of the current workspace
     */
    @Override
    public boolean flipCompletionStatus() {
        this.complete = !this.complete;
        return this.complete;
    }

    /**
     * Checks if all workspaces in the tasks list are complete.
     *
     * @return True if all workspaces in tasks are complete.
     */
    private boolean checkTasks() {
        boolean results = false;
        for (Workspace w: this.tasks) {
            results = w.isWorkspaceComplete();
            if (!results) {
                break;
            }
        }
        return results;
    }

    /**
     * Method that is called by one of the Tasks' workspaces in the task list.
     * It checks if all other workspaces are complete and if so, marks the
     * current Task as complete.
     *
     * @param workspace Workspace that has just been completed
     * @return          True if current workspace is complete, fale otherwise
     */
    boolean isFinished(final Workspace workspace) {
        this.complete = this.checkTasks();
        return this.complete;
    }

    /**
     * This creates a new workspace in the current task. A new
     * workspace must first be created, then added to the list
     * of workspaces that the currnet task is tracking. Finally,
     * the that task assigns itself as the parent of the newly created
     * task
     *
     * @return The newly created task
     */
    @Override
    public Workspace createWorkspace(final String workspaceName) {
        // Create a new task and add to the current tasks list
        Workspace newTask = new Task(workspaceName);
        tasks.add(newTask);
        newTask.setParent(this);
        return newTask;
    }

    /**
     * Used to display the task in a readable format to the console.
     * The format is currently,
     * DISPLAYHEADER
     * * {Name of Workspace}
     *   * Sub task 1
     *   * Sub task 2
     *     * Sub sub task 1
     *     * Sub sub task 2
     *   * Sub task 3
     * DISPLAYHEADER
     *
     * @return String to display
     */
    @Override
    public String toString() {
        StringBuilder display = new StringBuilder(DISPLAYHEADER);
        display.append(this.name);
        for (Workspace a: tasks) {
            display.append("\n  * " + ((Task) a).display("    "));
        }
        display.append(DISPLAYHEADER);
        return display.toString();
    }

    /**
     * Utility funciton used in toString(). Returns a string of the current
     * task and all its workspaces in tasks in the format,
     * * Task 1
     *   * Sub task 1
     *     * Sub sub task 1
     *     * Sub sub task 2
     *   * Sub task 2
     *
     * @param sep   Spaces between start of line and where the * appears
     * @return      A string to display
     */
    private String display(final String sep) {
        StringBuilder disp = new StringBuilder();
        disp.append(this.getName());
        for (Workspace a: tasks) {
            disp.append("\n" + sep + "* " + ((Task) a).display(sep + "  "));
        }
        return disp.toString();
    }

    /**
     * Moves the current Task into another Tasks list of tasks. This is done by updating the current
     * Tasks parent, adding it to the list of tasks of it's new parent and removing it from the list
     * of tasks of its old parent.
     *
     * @return  The tasks new parent
     */
    @Override
    public Workspace moveWorkspace() {
        // TODO Auto-generated method stub
        return null;
    }
}
