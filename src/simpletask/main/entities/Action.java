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
     * Unique long value that is used to ensure that this is the correct object
     * during serialisation.
     */
    private static final long serialVersionUID = 4278080741841613065L;

    // Constructors
    /**
     * Private constructor, don't know if needed yet.
     */
    private Action() {

    };
    /**
     * Creates an action given a name.
     *
     * @param nm    Name of action
     */
    public Action(final String nm) {
        this.name = nm;
    }

    @Override
    public boolean isWorkspaceComplete() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean flipCompletionStatus() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    /**
     * Returns name of action.
     *
     * @return Name of Action
     */
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
     * Sets the parent of current task.
     *
     * @param   parent  New parent of task.
     */
    //@Override
    private void setParent(final Workspace parent) {
        this.parent = parent;

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
}
