package simpletask.main.app;

import simpletask.main.entities.Task;
import simpletask.main.entities.Options;
import simpletask.main.entities.Workspace;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class from which application is started.
 *
 * @author Matthew Taggart
 */
public final class App {
    // Application constants
    /**
     * Switch for debug statements.
     */
    private static final boolean DEBUG = true;

    /**
     * Private constroter so the application class cannot be extended.
     */
    private App() {

    };

    /**
     * Main execution function of application.
     *
     * @param args  Array of strings from command line
     */
    public static void main(final String[] args) {
        Scanner sc = new Scanner(System.in);
        // Create your initial workspace
        System.out.print("Enter the name of your task: ");
        Workspace workspace = new Task(sc.nextLine());

        // Load workspace
        // System.out.println("Loading workspace from SavedWorkspace/workspace.ser");
        // Workspace workspace = loadWorkspace("SavedWorkspace/workspace.ser");

        Options option;
        String st;
        do {
            displayOptions();
            System.out.println("What would you like to do?");
            st = sc.nextLine().toUpperCase();
            try {
                option = Options.valueOf(st);
            } catch (IllegalArgumentException e) {
                System.out.println(st + " is an invalid option. Try again");
                option = Options.DONOTHING;
            }

            switch (option) {
                case ADD:
                    addWorkspace((Task) workspace, sc);
                    break;
                case MOVE:
                    workspace = moveIntoWorkspace(workspace, sc);
                    break;
                case PRINT:
                    System.out.println(workspace);
                    break;
                case SAVE:
                    saveWorkspace(workspace);
                    break;
                case QUIT:
                    quitSession(sc);
                    break;
                case DONOTHING:
                    break;
                default:
                    break;
            }
        } while (option != Options.QUIT);
    }

    /**
     * Adds a workspace into the the Task passed in.
     *
     * @param workspace The parent task
     * @param sc        Scanner to read input from
     */
    private static void addWorkspace(final Task workspace, final Scanner sc) {
        System.out.print("Enter your subtask: ");
        workspace.createWorkspace(sc.nextLine());
    }

    /**
     * Given a workspace, give the option to the user to move up into its parent workspace
     * or into one of its sub tasks if there are any.
     *
     * @param workspace Current workspace
     * @param sc        Scanner to read input from
     * @return          The workspace the user moved into
     */
    private static Workspace moveIntoWorkspace(final Workspace workspace, final Scanner sc) {
        Workspace newWorkspace = workspace;
        System.out.println("0 " + workspace.getParent().getName());
        ArrayList<Workspace> act = workspace.getTasks();
        for (int i = 0; i < act.size(); i++) {
            System.out.println((i + 1) + " " + act.get(i).getName());
        }
        System.out.print("Choose a task to move into: ");
        int i = Integer.parseInt(sc.nextLine());
        if (i == 0) {
            newWorkspace = workspace.getParent();
        } else {
            try {
                newWorkspace = act.get(i - 1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(i + " is not a valid option");
            }
        }
        return newWorkspace;
    }

    /**
     * Save the workspace.
     *
     * @param   w   The workspace to save
     * @return      True if save was successful, false otherwise
     */
    public static boolean saveWorkspace(final Workspace w) {
        try {
            FileOutputStream fileOut = new FileOutputStream("SavedWorkspace/workspace.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(w);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in SavedWorkspace/workspace.ser");
            return true;
         } catch (IOException i) {
            i.printStackTrace();
            return false;
         }
    }

    /**
     * Load in the workspace in the given file.
     *
     * @param file  Path to workspace on disk
     * @return      The Task in the file or null if failed
     */
    public static Workspace loadWorkspace(final String file) {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Workspace w = (Workspace) in.readObject();
            in.close();
            fileIn.close();
            return w;
         } catch (IOException i) {
            i.printStackTrace();
            return null;
         } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
         }
    }

    /**
     * Prints a message to say session is ending. Releases any resources being
     * used by the session.
     *
     * @param sc    Scanner resource being used by session
     */
    private static void quitSession(final Scanner sc) {
        System.out.println("Exiting session...");
         sc.close();
    }

    /**
     * Utility funciton to display current list of options avaliable to the user
     * on what they can do with their tasks. This function simply prints these options
     * to stdout. These options can be found in the Options enumeration.
     *
     * @see Options
     */
    private static void displayOptions() {
        System.out.println();
        for (Options a: Options.values()) {
            if (a != Options.DONOTHING) {
                System.out.println(a);
            }
        }
        System.out.println();
    }

    /**
     * Utility to display a message to stdout when the DEBUG flag is true.
     *
     * @param msg   Message to display to console
     */
    private static void debugLog(final String msg) {
        if (DEBUG) {
            System.out.println("[Debug] - " + msg);
        }
    }
}
