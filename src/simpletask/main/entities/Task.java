package simpletask.main.entities;

import java.time.LocalDateTime;
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
    // Class variables
    /**
     * Unique long value that is used to ensure that this is the correct object
     * during serialisation.
     */
    private static final long serialVersionUID = -8072249000932772481L;
    /**
     * String that is appended to the task when it is being printed
     * to the console. Not functional, just for neatness.
     */
    private static final String DISPLAYHEADER = "\n-------------------------------------\n";
    /**
     * Maximum value of importance for a task. Currently set to 10.
     */
    private static final byte MAXIMPORTANCE = 10;
    /**
     * Minimum value of importance for a task. Currently set to 0, this avoids negative importance.
     */
    private static final byte MINIMPORTANCE = 0;
    // Instance Variables
    /**
     * Name of task.
     */
    private String name;
    /**
     * Description of task.
     */
    private String description;
    /**
     * Due date of task.
     */
    private LocalDateTime dueDate;
    /**
     * Flag to mark if task is complete.
     */
    private boolean complete;
    /**
     * Priority of task. From 1-10. Using byte to minimise storage
     */
    private byte priority;
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

    // Constructors
    /**
     * The basic constructor for a task. At most, a task needs a name, dueDate and parent.
     * It needs all three as it's equals method uses these three to determine equlity. By 
     * default, a task is called "Null Task" a task is it's own parent and it's due at the
     * time of creation.
     */
    private Task() {
        name = "Null Task";
        parent = this;
        dueDate = LocalDateTime.now();
    }
    /**
     * Creates a task with the inputted name.
     *
     * @param name  The name of the task
     */
    public Task(final String name) {
        this();
        this.name = name;
    }

    // Methods
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
     * Returns the user definied importance of task.
     *
     * @return  Priority of task
     */
    public int getPriority() {
        return (int) this.priority;
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
     * Returns due date of task.
     *
     * @return  Returns the date and time that the task is due
     */
    public LocalDateTime getDueDate() {
        return this.dueDate;
    }

    /**
     * Sets the parent of the current task to the inputted workspace.
     *
     *  @param parent    The parent workspace
     */
    //@Override
    private void setParent(final Workspace parent) {
        this.parent = parent;
    }

    /**
     * Sets the description attribute.
     *
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }
    /**
     * Sets the due date of the task.
     *
     * @param year      Year that task is due
     * @param month     Month that task is due
     * @param day       Day in month that task is due
     * @param hour      Hour task is due
     * @param minute    Minute task is due
     */
    public void setDueDate(final int year, final int month, final int day, final int hour, final int minute) {
        this.dueDate = LocalDateTime.of(year, month, day, hour, minute);
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
     * Sets the priority for the task. Cannot have negative importance or greater than 10.
     *
     * @param imp   Number to set priority to
     * @return      True if priority set correctly
     * @throws      InvalidPriorityException if invalid priority is entered
     */
    public boolean setPriority(final int imp) throws InvalidPriorityException {
        // Cannot have negative importacne or importance greater than 10
        if (imp > MAXIMPORTANCE || imp < MINIMPORTANCE) {
            throw new InvalidPriorityException("Cannot have a negative importance or importance greater than 10");
        } else {
            this.priority = (byte) imp;
        }
        return true;
    }
    /**
     * Returns description of task.
     *
     * @return  The description of this task
     */
    public String getDescription() {
        return this.description;
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
     * @param   workspaceName   New workspace to move into current workspace
     * @return                  The newly created task
     */
    //@Override
    public Workspace createWorkspace(final Workspace workspaceName) {
        // Add workspace to list of workspaces and set its parent
        workspaceName.moveWorkspace(this);
        return workspaceName;
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
     * @param   target  The current tasks new parent
     * @return          True if move was successful
     */
    @Override
    public boolean moveWorkspace(final Task target) {
        // Only tasks can have childern so we know the result of this will always be a task
        Task oldParent = (Task) this.getParent();
        // Task will be it's own parent if no parent exists
        if (oldParent != this) {
            oldParent.tasks.remove(this);
        }
        this.setParent(target);
        target.addToTask(this);
        return true;
    }
    /**
     * Searches the list of tasks for the workspace entered.
     *
     * @param   workspace   Workspace to find
     * @return              True if found, false otherwise.
     */
    @Override
    public boolean searchWorkspaces(final Workspace workspace) {
        for (Workspace w: this.getTasks()) {
            if (w.equals(workspace)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Overrides the default object equals method. Two tasks are equal if they share the same
     * name, dueDate and parent. Note, the parents must be the same instance.
     *
     * @param   obj Object to check for equality
     * @return      True if object is equal to this Task, false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Task) {
            return name.equals(((Task) obj).name)
                && dueDate.equals(((Task) obj).dueDate)
                // Parents must be same instance to ensure two objects are truely the same and not
                // in two seperate workspaces.
                && parent == ((Task) obj).parent;
        } else {
            return false;
        }
    }

    /**
     * Removes workspace from list of tasks.
     *
     * @param workspace Workspace to remove from list of tasks
     * @return          True if workspace successfully removed
     */
    public boolean deleteWorkspace(final Workspace workspace) {
        this.tasks.remove(workspace);
        return true;
    }
    /**
     * Adds workspace to list of tasks.
     *
     * @param workspace Workspace to add to list of workspaces
     * @return          True if workspace is added successfully
     */
    protected boolean addToTask(final Workspace workspace) {
        return this.tasks.add(workspace);
    }
}
