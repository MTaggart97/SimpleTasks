package simpletask.main.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to hold the node (task/action) data that is used by the WorkspaceManager to send data. It is
 * essentially a summary of a WorkspaceNode. This is so only the WorkspaceManager has control over
 * the actual workspace. All other classes outside the entities package will need to interact with the
 * workspace via API's and NodeData instances.
 *
 * @author Matthew Taggart
 */
public class NodeData implements Serializable {
    /**
     * UID used when searlising the object.
     */
    private static final long serialVersionUID = -907719624791406245L;
    /**
     * Map of strings to hold node data. Keys are described in NodeKeys enum.
     */
    private Map<NodeKeys, String> nodeData;
    /**
     * Creates a NodeData instance given a WorkspaceNode.
     *
     * @param workspace The workspaceNode to convert to NodeData
     */
    public NodeData(final WorkspaceNode workspace) {
        this();
        setNodeData(workspace);
    }
    /**
     * Initialises the map used to hold the node data.
     */
    public NodeData() {
        nodeData = new HashMap<>();
    }

    //#region [Getters]
    /**
     * Gets the value at a given key.
     *
     * @param nKeys The key to extract
     * @return  The value at the inputted key
     */
    public String getAttr(final NodeKeys nKeys) {
        return nodeData.get(nKeys);
    }
    //#endregion [Getters]

    //#region [Setters]
    /**
     * Given a WorkspaceNode, set the values in the nodeData map to the corresponding
     * data in the WorkspaceNode.
     *
     * @param workspace The WorkspaceNode to summarise
     */
    public void setNodeData(final WorkspaceNode workspace) {
        for (NodeKeys nKeys : NodeKeys.values()) {
            nodeData.put(nKeys, workspace.getAttr(nKeys));
        }
    }
    /**
     * Sets the value of a given key with the inputted value.
     *
     * @param nKeys The key to set
     * @param data  The value to set it to
     * @return      The string (value) that was set
     */
    public String setAttr(final NodeKeys nKeys, final String data) {
        return nodeData.put(nKeys, data);
    }
    //#endregion [Setters]

    /**
     * Override of the equals method. Two NodeData objects are considered equal if all values
     * of it's keys are the same i.e. if the nodeData map is the same for both.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof NodeData)) {
            return false;
        }

        // Compare each key in each dictionary
        for (NodeKeys nKeys: NodeKeys.values()) {
            if (null != this.getAttr(nKeys) && null != ((NodeData) obj).getAttr(nKeys)) {
                if (!(this.getAttr(nKeys).equals(((NodeData) obj).getAttr(nKeys)))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Override of the hashcode function. Currently just calls the WorkspaceNode hashCode.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
