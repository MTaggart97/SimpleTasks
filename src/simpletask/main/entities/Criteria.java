package simpletask.main.entities;

/**
 * Class to be used when searching for Tasks and Actions in a list. The keys that can be
 * searched on are defined in the NodeKeys enumeration.
 *
 * @author  Matthew Taggart
 */
public class Criteria {
    /**
     * Dictionary containing the search keys and values. The search keys are defined in
     * the NodeKeys enumeration.
     */
    private NodeData dict;
    /**
     * Public construtor that initialises and empty dictionary.
     */
    public Criteria() {
        dict = new NodeData();
    }
    /**
     * Sets the search criteria of the given key.
     *
     * @param nKeys The key to set
     * @param data  The value to set the key to
     * @return      This criteria object
     */
    public Criteria addAttr(final NodeKeys nKeys, final String data) {
        dict.setAttr(nKeys, data);
        return this;
    }
    /**
     * To be used to compare a summarised version of a task to the dict. Returns true if all
     * values in the Criteria dictionary are matched (ignoring case).
     * <p>
     * In this case a match is when the two strings are exactly equal, ignoring case. No regex
     * is supported.
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
