package simpletask.entities;

import java.util.ArrayList;

/**
 * Class that describes a singular unit of work. It cannot be broken up into
 * smaller sub tasks
 *
 * @author Matthew Taggart
 * @see Workspace
 */
public class Action implements Workspace {

    @Override
    public boolean isWorkspaceComplete() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean flipCompletionStatus() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Workspace createWorkspace(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * This should simply return itself.
     * @return An array list containing it
     */
    @Override
    public ArrayList<Workspace> getTasks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setParent(Workspace parent) {
        // TODO Auto-generated method stub

    }

    @Override
    public Workspace getParent() {
        // TODO Auto-generated method stub
        return null;
    }
}
