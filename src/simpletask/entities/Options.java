package simpletask.entities;

/**
 * A list of options available to preform on tasks. Can
 * currently:
 * Quit      - Leave menu
 * Add       - Add a workspace to current workspace
 * Move      - Move into a Workspace
 * Print     - Display Workspace
 * Save      - Save Workspace
 * DoNothing - Does nothing
 *
 * @author Matthew Taggart
 */
public enum Options {
    /**
     * Option used to quit application.
     */
    QUIT,
    /**
     * Option used to add a workspace to the current.
     * workspace
     */
    ADD,
    /**
     * Option to move into another workspace.
     */
    MOVE,
    /**
     * Option to display the current workspace.
     */
    PRINT,
    /**
     * Option to save current workspace.
     */
    SAVE,
    /**
     * Do nothing.
     */
    DONOTHING
}
