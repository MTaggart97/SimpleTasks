package simpletask.main.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A WorkspaceNode is the template node of the Workspace multi-node tree. It contains
 * the basic info and methods any node will need if they are to be managed by the
 * WorkspaceManager.
 */
abstract class WorkspaceNode implements Serializable {
    //#region [Fields]
    /**
     * Unique long value that is used to ensure that this is the correct object
     * during serialisation.
     */
    private static final long serialVersionUID = -238221995955316939L;
    /**
     * Maximum value of importance for a WorkspaceNode.
     */
    protected static final int MAXIMPORTANCE = 10;
    /**
     * Minimum value of importance for a WorkspaceNode.
     */
    protected static final int MINIMPORTANCE = 0;
    /**
     * Name of WorkspaceNode.
     */
    protected String name;
    /**
     * Description of WorkspaceNode.
     */
    protected String description;
    /**
     * Due date of WorkspaceNode.
     */
    protected LocalDateTime dueDate;
    /**
     * Flag to mark if WorkspaceNode is complete.
     */
    protected boolean complete;
    /**
     * Priority of WorkspaceNode. Can be any value between MINIMPORTANCE and MAXINIMPORTANCE.
     */
    protected int priority;
    /**
     * Parent of current WorkspaceNode.
     */
    protected WorkspaceNode parent;
    //#endregion [Fields]

    //#region [Getters]
    /**
     * Returns a copy of the name of the WorkspaceNode.
     *
     *  @return Name of WorkspaceNode as string
     */
    protected String getName() {
        return new String(this.name);
    }
    /**
     * Returns the user definied priority of WorkspaceNode.
     *
     * @return  Priority of WorkspaceNode
     */
    protected int getPriority() {
        return this.priority;
    }
    /**
     * Returns the completion status of WorkspaceNode.
     *
     * @return  True if node complete, false otherwise
     */
    protected boolean getComplete() {
        return this.complete;
    }
    /**
     * Returns a copy of the description of WorkspaceNode.
     *
     * @return  The description of this task
     */
    protected String getDescription() {
        return this.description;
    }
    /**
     * Returns due date of WorkspaceNode.
     *
     * @return  Returns the date and time that the WorkspaceNode is due
     */
    protected LocalDateTime getDueDate() {
        return this.dueDate;
    }
    /**
     * Returns the parent of current WorkspaceNode.
     *
     *  @return The current WorkspaceNodes parent
     */
    protected WorkspaceNode getParent() {
        return this.parent;
    }
    /**
     * Used to get the list of nodes that the current node is a parent of. This method
     * is abstract since not all nodes will maintain a list of other nodes (e.g. Action).
     *
     * @return  List of WorkspaceNodes that current node is parent of
     */
    protected abstract ArrayList<WorkspaceNode> getTasks();
    /**
     * 
     * @param nKeys
     * @return
     */
    protected String getAttr(final NodeKeys nKeys) {
        String res;
        switch(nKeys) {
            case NAME:        res = getName();                         break;
            case DESCRIPTION: res = getDescription();                  break;
            case PRIORITY:    res = String.valueOf(getPriority());     break;
            case TYPE:        res = getClass().getSimpleName();        break;
            case TASKS:       res = String.valueOf(getTasks().size()); break;
            case DUEDATE:     res = getDueDate().toString();           break;
            case COMPLETE:    res = String.valueOf(getComplete());     break;
            default:          res = "No Value Set";                    break;
        }
        return res;
	}
    //#endregion [Getters]

    //#region [Setters]
    /**
     * Renames WorkspaceNode.
     *
     * @param name  Name of WorkspaceNode
     */
    protected void setName(final String name) {
        this.name = name;
    }
    /**
     * Sets the description attribute.
     *
     * @param description the description to set
     */
    protected void setDescription(final String description) {
        this.description = description;
    }
    /**
     * Sets the due date of the WorkspaceNode.
     *
     * @param year      Year that WorkspaceNode is due
     * @param month     Month that WorkspaceNode is due
     * @param day       Day in month that WorkspaceNode is due
     * @param hour      Hour WorkspaceNode is due
     * @param minute    Minute WorkspaceNode is due
     */
    protected void setDueDate(final int year, final int month, final int day, final int hour, final int minute) {
        this.dueDate = LocalDateTime.of(year, month, day, hour, minute);
    }
    /**
     * Sets dueDate.
     *
     * @param   dt  DateTime to set dueDate to
     */
    protected void setDueDate(final LocalDateTime dt) {
        this.dueDate = dt;
    }
    /**
     * Sets the priority for the WorkspaceNode. Default implementation is to ensure that the
     * priority does not exceed MAXIMPORTANCE or is less than MINIMPORTANCE. Throws InvalidPriorityException
     * if number entered is outside of this range.
     *
     * @param imp   Number to set priority to
     * @throws      InvalidPriorityException if invalid priority is entered
     */
    protected void setPriority(final int imp) throws InvalidPriorityException {
        // Cannot have negative importacne or importance greater than 10
        if (imp > MAXIMPORTANCE || imp < MINIMPORTANCE) {
            throw new InvalidPriorityException("Cannot have a negative importance or importance greater than " + MAXIMPORTANCE);
        } else {
            this.priority = imp;
        }
    }
    //#endregion [Setters]

    //#region [Implementation]
    //#region [Default]
    /**
     * Flips the completion status of the current WorkspaceNode. Can be used
     * by the user if they wish to manually change the completion status
     * of the current workspace.
     *
     * @return The completion status of the current WorkspaceNode
     */
    protected boolean flipCompletionStatus() {
        this.complete = !this.complete;
        return this.complete;
    }
    //#endregion [Default]
    //#region [Abstract]
    /**
     * Removes the current WorkspaceNode from whatever list of nodes it may be in.
     *
     * @return  True if WorkspaceNode is successfully deleted
     */
    protected abstract boolean delete();
    /**
     * Moves the current WorkspaceNodes into another nodes list of tasks.
     *
     * @param   target  The new parent of the node
     * @return          True if move was successful
     */
    protected abstract boolean moveWorkspace(final WorkspaceNode target);
    /**
     * Returns completion status, taking into account the completion status of any nodes
     * that may be maintained by this one.
     *
     * @return          True if current workspace is complete, fale otherwise
     */
    protected abstract boolean isFinished();
    /**
     * Create a new WorkspaceNode and add it to list of nodes to maintain.
     *
     * @param   workspaceName   New workspace to move into current workspace
     * @return                  Newly added WorkspaceNode
     */
    protected abstract WorkspaceNode createWorkspace(final WorkspaceNode workspaceName);
    /**
     * Searches the list of tasks for the workspace entered.
     *
     * @param   workspace   WorkspaceNode to find
     * @return              True if found, false otherwise.
     */
    protected abstract boolean searchWorkspaces(final WorkspaceNode workspace);
    //#endregion [Abstract]
    //#endregion [Implementation]
}