package simpletask.main.entities;

import java.util.ArrayList;

/**
 * Class that describes a singular unit of work. It cannot be broken up into
 * smaller sub tasks
 *
 * @author Matthew Taggart
 * @see Workspace
 */
public class Action implements Workspace {
    /**
     * Name of action.
     */
    private String name;
    /**
     * Parent of current Action.
     */
    private Workspace parent;
    /**
     * Boolean flag to signal if Action is complete.
     */
    private boolean isComplete;
    /**
     * Description of Action.
     */
    private String description;
    /**
     * Unique long value that is used to ensure that this is the correct object
     * during serialisation.
     */
    private static final long serialVersionUID = 4278080741841613065L;

    // Constructors
    /**
     * Creates an action given a name.
     *
     * @param nm    Name of action
     */
    public Action(final String nm) {
        this.name = nm;
    }
    /**
     * Returns true if Action is complete.
     *
     * @return  True if Action complete, false otherwise
     */
    @Override
    public boolean isWorkspaceComplete() {
        return isComplete;
    }
    /**
     * Flips completion status and returns new status.
     *
     * @return  True if task is complete, false otherwise
     */
    @Override
    public boolean flipCompletionStatus() {
        isComplete = !isComplete;
        return isComplete;
    }
    /**
     * Returns name of action.
     *
     * @return Name of Action
     */
    @Override
    public String getName() {
        return this.name;
    }
    /**
     * This should simply return itself.
     * @return An array list containing it
     */
    @Override
    public ArrayList<Workspace> getTasks() {
        ArrayList<Workspace> result = new ArrayList<>();
        result.add(this);
        return result;
    }
    /**
     * Returns description of task.
     *
     * @return  Description of task
     */
    public String getDescription() {
        return description;
    }
    /**
     * Returns parent of current action.
     *
     * @return  Parent Workspace of current Action
     */
    @Override
    public Workspace getParent() {
        return this.parent;
    }
    /**
     * Sets the parent of current task.
     *
     * @param   parent  New parent of task.
     */
    private void setParent(final Workspace parent) {
        this.parent = parent;

    }
    /**
     * Sets the description of the current Action.
     *
     * @param   description     Description of Action
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Moves current action into a new workspace.
     *
     * @param   target  The new parent of the current action
     * @return          True if Action moved successfully
     */
    @Override
    public boolean moveWorkspace(final Task target) {
        // Only tasks can have childern so we know the result of this will always be a task
        Task oldParent = (Task) this.getParent();
        if (oldParent != null) {
            oldParent.deleteWorkspace(this);
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
        msg.append("\n -- Completion Status: " + this.isComplete);
        msg.append("\n -- " + this.description);
        return msg.toString();
    }
}
