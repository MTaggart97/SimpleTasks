package simpletask.main.entities;

/**
 * Enum containing all keys for a WorkspaceNode. They are also used in the NodeData
 * and Criteria classes. These keys are used to describe a node and make up the complete
 * set of user editable attributes that a node has.
 * <p>
 * If a WorkspaceNode is to have any more user modifiable attributes (e.g. image) then they need to be
 * added here first.
 *
 * @author Matthew Taggart
 */
public enum NodeKeys {
    /**
     * The name of the node.
     */
    NAME,
    /**
     * The description of the node.
     */
    DESCRIPTION,
    /**
     * The duedate of the node.
     */
    DUEDATE,
    /**
     * The priority of the node.
     */
    PRIORITY,
    /**
     * The completion status of the node.
     */
    COMPLETE,
    /**
     * The sub-tasks (children) of the node.
     */
    TASKS,
    /**
     * The type of the node i.e. Task or Action.
     */
    TYPE
}
