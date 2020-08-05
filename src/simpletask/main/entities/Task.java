package simpletask.main.entities;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A Task object is a WorkspaceNode that can maintain a list of other nodes. It has
 * the same attribtes as any other node (name, description, due date etc.) but has
 * the ability to create new nodes and place them in it's list of nodes. The main
 * use of this class is to group similar Tasks/Actions. For example, ToD0, inProgress,
 * AssignmentOne, SprintOne etc. It can be a way to tag Tasks/Actions as well.
 *
 * @author Matthew Taggart
 */
class Task extends WorkspaceNode {
    //#region [Fields]
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
     * A list of tasks maintianed by current task. For a task to be
     * marked as complete, all its tasks must be marked as complete
     */
    private ArrayList<WorkspaceNode> tasks = new ArrayList<>();
    /**
     * Parent of current task. This is generally used to keep track of
     * the current task, i.e. todo, in progress, Assignment 1 etc. It shadows the field
     * in WorkspaceNode. Since only Tasks can have nodes, it is safe to assume that any
     * parent will be a Task.
     */
    protected Task parent = null;
    //#endregion [Fields]

    //#region [Constructors]
    /**
     * The basic constructor for a task. At most, a task needs a name, dueDate and parent.
     * It needs all three as it's equals method uses these three to determine equlity. By
     * default, a task is called "Null Task" a task is it's own parent and it's due at the
     * time of creation.
     */
    Task() {
        name = "Null Task";
        parent = this;
        dueDate = LocalDateTime.now();
    }
    /**
     * Creates a task with the inputted name.
     *
     * @param name  The name of the task
     */
    Task(final String name) {
        this();
        this.name = name;
    }
    //#endregion [Constructors]

    //#region [Getters]
    /**
     * Returns the list of all workspaces contained in the current task.
     *
     *  @return All workspaces tracked by current task
    */
    protected ArrayList<WorkspaceNode> getTasks() {
        return tasks;
    }
    /**
     * Returns the parent of current task.
     *
     *  @return The current tasks parent
     */
    @Override
    protected Task getParent() {
        return this.parent;
    }
    //#endregion [Getters]

    //#region [Setters]
    /**
     * Sets the parent of the current task to the inputted workspace. A task is resposible for
     * setting its own parent and as such, this method is private. Preventing any other class from
     * changing it's parent and potentially dereferencing it.
     *
     *  @param parent    The parent workspace
     */
    private void setParent(final Task parent) {
        this.parent = parent;
    }
    //#endregion [Setters]

    // Implementation Methods
    /**
     * Checks if all workspaces in the tasks list are complete. Used when checking
     * if the current Task is complete.
     *
     * @return True if all workspaces in tasks are complete.
     */
    private boolean checkTasks() {
        boolean results = false;
        for (WorkspaceNode w: this.tasks) {
            results = w.getComplete();
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
     * @return          True if current workspace is complete, fale otherwise
     */
    @Override
    protected boolean isFinished() {
        this.complete = this.checkTasks();
        return this.complete;
    }
    /**
     * This creates a new workspace in the current task. This is done by
     * using the moveWorkspace method on the new workspace. This moves the new
     * workspace into the current Task.
     *
     * @param   workspaceName   New workspace to move into current workspace
     * @return                  The newly created task
     */
    @Override
    protected WorkspaceNode createWorkspace(final WorkspaceNode workspaceName) {
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
        display.append("| " + this.name + "\n| ");
        for (WorkspaceNode a: tasks) {
            display.append(a.getName() + "\t");
        }
        display.append(DISPLAYHEADER);
        return display.toString();
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
    protected boolean moveWorkspace(final WorkspaceNode target) {
        // If target is not an instance of Task, then currnet Task cannot be moved
        if (!(target instanceof Task)) {
            return false;
        };
        // Only tasks can have childern so we know the result of this will always be a task
        Task oldParent = this.getParent();
        // Task will be it's own parent if no parent exists
        if (oldParent != this) {
            oldParent.tasks.remove(this);
        }
        this.setParent((Task) target);
        ((Task) target).addToTask(this);
        return true;
    }
    /**
     * Searches the list of tasks for the workspace entered. It will also check if current
     * Task is equal to workspace entered.
     *
     * @param   workspace   Workspace to find
     * @return              True if found, false otherwise.
     */
    @Override
    protected boolean searchWorkspaces(final WorkspaceNode workspace) {
        if (this.equals(workspace)) {
            return true;
        }
        for (WorkspaceNode w: this.getTasks()) {
            // if (w.equals(workspace)) {
            //     return true;
            // }
            if (w.searchWorkspaces(workspace)) {
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
     * Removes workspace from list of tasks if the workspace is there. Note, this assumes that the
     * workspace entered is the same instance as the workspace you are trying to remove. If not, this
     * will fail to remove workspace you want and will simply call the delte method on the workspace
     * you passed in.
     *
     * @param workspace Workspace to remove from list of tasks
     * @return          True if workspace successfully removed
     */
    protected boolean removeWorkspace(final WorkspaceNode workspace) {
        boolean found = this.searchWorkspaces(workspace);
        if (found) {
            return workspace.delete();
        }
        return found;
    }
    /**
     * Adds workspace to list of tasks.
     *
     * @param workspace Workspace to add to list of workspaces
     * @return          True if workspace is added successfully
     */
    protected boolean addToTask(final WorkspaceNode workspace) {
        return this.tasks.add(workspace);
    }
    /**
     * Deletes Task by first deleting all Workspaces in its list. It then tells its parent
     * to remove it from their list and sets its parent to null.
     *
     * @return  True if Task is successfully deleted
     */
    @Override
    protected boolean delete() {
        boolean fin = false;
        while (!tasks.isEmpty()) {
            fin = tasks.get(0).delete();
            if (!fin) {  // If delete fails, stop immediatly
                return fin;
            }
        }
        parent.getTasks().remove(this);
        this.setParent(null);
        return true;
    }

    @Override
    protected Action asAction() throws InvalidClassException {
        if (tasks.size() == 0) {
            Action action = new Action(this.name);
            action.setDescription(this.description);
            action.setComplete(String.valueOf(this.complete));
            action.parent = (Task) this.parent;
            ((Task) action.parent).addToTask(action);
            action.setDueDate(this.dueDate);
            action.setPriority(this.priority);
            
            // Delete this object
            this.delete();

            return action;
        } else {
            throw new InvalidClassException("Cannot convert Task to Action as Task still has sub tasks");
        }
    }

    @Override
    protected Task asTask() {
        return this;
    }
}
