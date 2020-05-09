package simpletask.main.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
     * Due date of Action.
     */
    private LocalDateTime dueDate;
    /**
     * Boolean flag to signal if Action is complete.
     */
    private boolean isComplete;
    /**
     * Priority of Action.
     */
    private byte priority;
    /**
     * Description of Action.
     */
    private String description;
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

    // Constructors
     /**
     * The basic constructor for an action. At most, an action needs a name, dueDate and parent.
     * It needs all three as it's equals method uses these three to determine equlity. By
     * default, an action is called "Null Action" an action is it's own parent and it's due at the
     * time of creation.
     */
    private Action() {
        this.name = "Null Action";
        parent = this;
        dueDate = LocalDateTime.now();
    }
    /**
     * Creates an action given a name.
     *
     * @param nm    Name of action
     */
    public Action(final String nm) {
        this();
        this.name = nm;
    }

    // Getters
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
     *
     * @return An array list containing itself
     */
    @Override
    public ArrayList<Workspace> getTasks() {
        ArrayList<Workspace> result = new ArrayList<>();
        result.add(this);
        return result;
    }
    /**
     * Returns due date of Action.
     *
     * @return  Returns the date and time that the Action is due
     */
    public LocalDateTime getDueDate() {
        return this.dueDate;
    }
    /**
     * Returns priority of Action.
     *
     * @return  Priority of Action
     */
    @Override
    public int getPriority() {
        return (int) this.priority;
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
     * Returns details of this Task.
     *
     * @return  Map of details
     */
    @Override
    public Map<String, String> getDetails() {
        Map<String, String> dict = new HashMap<String, String>();

        dict.put("Name", this.getName());
        dict.put("Priority", String.valueOf(this.getPriority()));
        dict.put("Type", "Action");

        return dict;
    }

    // Setters
    /**
     * Sets the parent of current task.
     *
     * @param   parent  New parent of task.
     */
    private void setParent(final Workspace parent) {
        this.parent = parent;

    }
    /**
     * Sets dueDate of Action.
     *
     * @param   dt  Date Time to set dueDate to
     */
    @Override
    public void setDueDate(final LocalDateTime dt) {
        this.dueDate = dt;
    }
    /**
     * Sets priority of Action.
     *
     * @param   imp Importance/Priority of Action
     */
    @Override
    public void setPriority(final int imp) {
        // Cannot have negative importacne or importance greater than 10
        if (imp > MAXIMPORTANCE || imp < MINIMPORTANCE) {
            throw new InvalidPriorityException("Cannot have a negative importance or importance greater than " + MAXIMPORTANCE);
        } else {
            this.priority = (byte) imp;
        }
    }
    /**
     * Renames Action.
     *
     * @param   name    Name to rename Action to.
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Sets the due date of the Action.
     *
     * @param year      Year that Action is due
     * @param month     Month that Action is due
     * @param day       Day in month that Action is due
     * @param hour      Hour Action is due
     * @param minute    Minute Action is due
     */
    public void setDueDate(final int year, final int month, final int day, final int hour, final int minute) {
        this.dueDate = LocalDateTime.of(year, month, day, hour, minute);
    }
    /**
     * Sets the description of the current Action.
     *
     * @param   description     Description of Action
     */
    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    // Implentation Methods
    /**
     * Moves current action into a new workspace.
     *
     * @param   target  The new parent of the current action
     * @return          True if Action moved successfully
     */
    @Override
    public boolean moveWorkspace(final Task target) {
        Workspace oldParent = this.getParent();
        // Ensure oldParent is a Task before attempting to remove from its list.
        if (oldParent instanceof Task && oldParent != null) {
            ((Task) oldParent).removeWorkspace(this);
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
    /**
     * Since an Action does not have any sub workspaces, you cannot search it's list of workspaces.
     * As a result, this method will always return false.
     *
     * @param   workspace   Workspace to search for
     * @return              Will always return false
     */
    @Override
    public boolean searchWorkspaces(final Workspace workspace) {
        return false;
    }
    /**
     * Override of Object equals method. Two Actions are equal if they share the same name, dueDate and
     * parent. Note, the parents must be the same instance.
     *
     * @param   obj Object to compare against
     * @return      True if this Action is equal to Object passed in, false otherwise.
     */
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
    public boolean delete() {
        parent.getTasks().remove(this);
        this.setParent(null);
        return true;
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
}
