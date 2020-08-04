package simpletask.main.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
// TODO: Document this
/**
 * Class to hold the node (task/action) data that is used by the WorkspaceManager to send data.
 *
 * @author Matthew Taggart
 */
public class NodeData  implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -907719624791406245L;
    /**
     * Map of strings to hold node data. Keys are described in NodeKeys enum.
     */
    private Map<NodeKeys, String> nodeData;
    /**
     * 
     * @param workspace
     */
    public NodeData(final WorkspaceNode workspace) {
        this();
        setNodeData(workspace);
    }
    /**
     * 
     */
    public NodeData() {
        nodeData = new HashMap<>();
    }

    //#region [Getters]
    /**
     * 
     * @param nKeys
     * @return
     */
    public String getAttr(final NodeKeys nKeys) {
        return nodeData.get(nKeys);
    }
    //#endregion [Getters]

    //#region [Setters]
    /**
     * 
     * @param workspace
     */
    public void setNodeData(final WorkspaceNode workspace) {
        System.out.println(workspace);
        System.out.println(nodeData);
        for(NodeKeys nKeys : NodeKeys.values()) {
            nodeData.put(nKeys, workspace.getAttr(nKeys));
        }
    }
    /**
     * 
     * @param nKeys
     * @param data
     * @return
     */
    public String setAttr(final NodeKeys nKeys, final String data) {
        return nodeData.put(nKeys, data);
    }
    //#endregion [Setters]

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
}