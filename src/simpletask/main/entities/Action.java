package simpletask.main.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that describes a singular unit of work. It cannot be broken up into
 * smaller sub tasks
 *
 * @author  Matthew Taggart
 * @see     Task
 */
class Action extends Task {
    //#region [Fields]
    /**
     * Unique long value that is used to ensure that this is the correct object
     * during serialisation.
     */
    private static final long serialVersionUID = 4278080741841613065L;
    /**
     * Maximum value of importance for a task. Currently set to 10.
     */
    private static final byte MAXIMPORTANCE = 10;
    /**
     * Minimum value of importance for a task. Currently set to 0, this avoids negative importance.
     */
    private static final byte MINIMPORTANCE = 0;
    //#endregion [Fields]

    //#region [Constructors]
     /**
     * The basic constructor for an action. At most, an action needs a name, dueDate and parent.
     * It needs all three as it's equals method uses these three to determine equlity. By
     * default, an action is called "Null Action" an action is it's own parent and it's due at the
     * time of creation.
     */
    Action() {
        this.name = "Null Action";
        parent = this;
        dueDate = LocalDateTime.now();
    }
    /**
     * Creates an action given a name.
     *
     * @param nm    Name of action
     */
    Action(final String nm) {
        this();
        this.name = nm;
    }
    //#endregion [Constructors]

    //#region [Getters]
    /**
     * This should null since an Action has no concept of task lists.
     *
     * @return Null
     */
    @Override
    public ArrayList<Task> getTasks() {
        return null;
    }
    //#endregion [Getters]

    //#region [Setters]
    private void setParent(final Task parent) {
        this.parent = parent;
    }
    //#endregion [Setters]

    // Implentation Methods
    /**
     * Moves current action into a new workspace.
     *
     * @param   target  The new parent of the current action
     * @return          True if Action moved successfully
     */
    @Override
    public boolean moveWorkspace(final Task target) {
        Task oldParent = this.getParent();
        // Ensure oldParent is a Task before attempting to remove from its list.
        if (oldParent instanceof Task && oldParent != null) {
            oldParent.removeWorkspace(this);
        }
        this.setParent(target);
        target.addToTask(this);
        return true;
    }
    /**
     * Overrides the toString method called when printing the object to stdout. Currently prints
     * the name, description and completion status of action.
     *
     * @return  String to display
     */
    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder(this.name);
        msg.append("\n -- Completion Status: " + this.complete);
        msg.append("\n -- " + this.description);
        return msg.toString();
    }
    /**
     * Since an Action does not have any sub workspaces, you cannot search it's list of workspaces.
     * As a result, this method will always return false.
     *
     * @param   workspace   Workspace to search for
     * @return              Will always return false
     */
    protected boolean searchWorkspaces(final Task workspace) {
        return false;
    }
    /**
     * Override of Object equals method. Two Actions are equal if they share the same name, dueDate and
     * parent. Note, the parents must be the same instance.
     *
     * @param   obj Object to compare against
     * @return      True if this Action is equal to Object passed in, false otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Action) {
            return name.equals(((Action) obj).name)
                && dueDate.equals(((Action) obj).dueDate)
                // Parents must be same instance to ensure two objects are truely the same and not
                // in two seperate workspaces.
                && parent == ((Action) obj).parent;
        } else {
            return false;
        }
    }
    /**
     * Deletes current Action by removing itself from its parents list then setting its parent to null.
     *
     * @return  True if Action deletes successfully, false otherwise
     */
    @Override
    protected boolean delete() {
        parent.getTasks().remove(this);
        this.setParent(null);
        return true;
    }
    /**
     * Returns null since an Action has no concept of a list of tasks to add a Task into.
     *
     * @param   workspaceName   New workspace to move into current workspace
     * @return                  Null
     */
    @Override
    protected Task createWorkspace(final Task workspaceName) {
        return null;
    }
}
