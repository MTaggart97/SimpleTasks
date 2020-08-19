/**
 * Package containing the Data Structures used to store the tasks. The base class of all Tasks and Actions
 * is the WorkspaceNode class. The Workspace can be thought of as a multi-node tree. With Tasks having
 * children and Action's having none.
 * <p>
 * To prevent classes outside this package from accessing the nodes directly, the data is transfered
 * using the NodeData class through the WorkspaceManager class.
 * <p>
 * The WorkspaceManager is a singleton class that other classes can use to interact with the workspace.
 */
package simpletask.main.entities;
