package entities;

import java.util.ArrayList;

/**
 * An interface used by all task classes.
 * @author Matthew Taggart
 */
public interface Action {
    /**
     * Used to check if action is complete or not.
     * @author MatthewTaggart
     * @return True if action is complete, fasle otherwise
     */
    boolean isActionComplete();
    /**
     * Used to update the completion status.
     * @param status    Status to change action to
     */
    void updateComplete(boolean status);
    /**
     * Used to create a new action.
     * @param name  Name of new action
     * @return The newly created action
     */
    Action createAction(String name);
    /**
     * Used to update the status of the action.
     * @param status    Status to update to
     * @return Updated status
     */
    Action updateStatus(String status);
    /**
     * Used to get name of action.
     * @return Name of action
     */
    String getName();

    ArrayList<Action> getTasks();

    void setParent(Task parent);

    Action getParent();
}
