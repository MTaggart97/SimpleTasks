package simpletask.main.entities;

/**
 * Class to be used when searching for Tasks and Actions in a list.
 *
 * @author  Matthew Taggart
 */
public class Criteria {
    /**
     * Dictionary containing the search keys and values.
     */
    private NodeData dict;
    /**
     * Public construtor that initialises and empty dictionary.
     */
    public Criteria() {
        dict = new NodeData();
    }
    /**
     * 
     * @param nKeys
     * @param data
     * @return
     */
    public Criteria addAttr(final NodeKeys nKeys, final String data) {
        dict.setAttr(nKeys, data);
        return this;
    }
    /**
     * To be used to compare a summarised version of a task to the dict. Returns true if all
     * values in the Criteria dictionary are matched (ignoring case).
     *
     * @param task  Summarised version of task
     * @return      True if it matches, false otherwise
     */
    protected boolean compare(final NodeData task) {
        boolean res = true;
        for (NodeKeys nKeys: NodeKeys.values()) {
            if (null != task.getAttr(nKeys) && null != dict.getAttr(nKeys)) {
                res &= task.getAttr(nKeys).equalsIgnoreCase(dict.getAttr(nKeys));
            }
        }
        return res;
    }
}