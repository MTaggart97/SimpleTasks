package entities;

import java.util.ArrayList;

public class Task implements Action {
    // Data fields
    private String name;
    private String description;
    //TODO: Look up best way to implement date
    // private Date dueDate;    
    private boolean complete;
    private byte importance;
    private String Status;
    private ArrayList<Action> tasks = new ArrayList<Action>();
    private Action parent = null;

    // Class constants
    private static final String displaySeparator = "\n-------------------------------------\n";

    /**
     * Empty constructor, don't know if needed yet
     */
    public Task() {

    }

    /**
     * The basic constructor for a task. At most, a task needs a name.
     * New tasks are created through the createAction method
     * @param name  The name of the task
     * @see
     */
    public Task(String name) {
        this.name = name;
        parent = this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ArrayList<Action> getTasks() {
        return tasks;
    }

    @Override
    public void setParent(Task parent) {
        this.parent = parent;
    }

    @Override
    public Action getParent() {
        return this.parent;
    }

    @Override
    public boolean isActionComplete() {
        // TODO Auto-generated method stub
        return complete;
    }

    @Override
    public void updateComplete(boolean status) {
        // TODO Auto-generated method stub
        this.complete = status;

    }

    @Override
    public Action createAction(String name) {
        // Create a new task and add to the current tasks list
        Task newTask = new Task(name);
        tasks.add(newTask);
        newTask.setParent(this);
        return newTask;
    }

    @Override
    public Action updateStatus(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        //StringBuilder display = new StringBuilder(displaySeparator);
        StringBuilder display = new StringBuilder();
        display.append(this.name);
        for(Action a: tasks) {
            //display.append("\n  * " + a.getName());
            display.append("\n  * " + ((Task)a).display("    "));
        }
        //display.append(displaySeparator);
        return display.toString();
    }

    private String display(String sep) {
        StringBuilder disp = new StringBuilder();
        disp.append(this.getName() );
        for(Action a: tasks) {
            disp.append("\n" + sep + "* " + ((Task)a).display(sep+"  "));
        }
        return disp.toString();
    }
}