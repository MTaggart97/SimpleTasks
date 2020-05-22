package simpletask.main.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to be used when searching for Tasks and Actions in a list.
 *
 * @author  Matthew Taggart
 */
public class Criteria {
    /**
     * Dictionary containing the search keys and values.
     */
    private Map<String, String> dict;
    /**
     * Public construtor that initialises and empty dictionary.
     */
    public Criteria() {
        dict = new HashMap<String, String>();
    }
    /**
     * Add name constraint to search criteria.
     *
     * @param name  Name to look for
     * @return      The newly update search Criteria
     */
    public Criteria addName(final String name) {
        dict.put("Name", name);
        return this;
    }
    /**
     * Add priority constraint to search criteria.
     *
     * @param priority  Priority to look for
     * @return          The newly update search Criteria
     */
    public Criteria addPriority(final String priority) {
        dict.put("Priority", priority);
        return this;
    }
    /**
     * Add type constraint to search criteria.
     *
     * @param type  Type to look for
     * @return      The newly update search Criteria
     */
    public Criteria addType(final String type) {
        dict.put("Type", type);
        return this;
    }
    /**
     * To be used to compare a summarised version of a task to the dict.
     *
     * @param task  Summarised version of task
     * @return      True if it matches, false otherwise
     */
    protected boolean compare(final Map<String, String> task) {
        boolean res = false;
        for (Map.Entry<String, String> entry: dict.entrySet()) {
            if (null != task.get(entry.getKey())) {
                res |= task.get(entry.getKey()).equals(entry.getValue());
            }
        }
        return res;
    }
}