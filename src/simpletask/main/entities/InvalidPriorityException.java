package simpletask.main.entities;

/**
 * Exception thrown when an invalid priority is given to a Workspace.
 */
public class InvalidPriorityException extends RuntimeException {
    /**
     * Auto generated serialVersionUID used for serialisation.
     */
    private static final long serialVersionUID = 892036384460218442L;
    /**
     * Constructor simply calls constructor for RuntimeException. Nothing fancy is needed.
     *
     * @param msg   Message passed into exception.
     */
    public InvalidPriorityException(String msg) {
        super("Invalid Priority Value - " + msg);
    }

}