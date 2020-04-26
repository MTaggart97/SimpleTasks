package simpletask.main.entities;

/**
 * Exception thrown when an invalid importance is given to a Workspace.
 */
public class InvalidImportanceException extends RuntimeException {
    /**
     * Auto generated serialVersionUID used for serialisation.
     */
    private static final long serialVersionUID = 892036384460218442L;
    /**
     * Constructor simply calls constructor for RuntimeException. Nothing fancy is needed.
     *
     * @param msg   Message passed into exception.
     */
    public InvalidImportanceException(String msg) {
        super("Invalid Importance Value - " + msg);
    }

}