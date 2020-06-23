package simpletask.main.app;

import simpletask.main.entities.WorkspaceManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Class from which application is started.
 *
 * @author Matthew Taggart
 */
public final class App {
    // Application constants
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
        // System.out.print("Enter the name of your task: ");
        // WorkspaceManager workspace = new WorkspaceManager(sc.nextLine());
        WorkspaceManager workspace = loadWorkspace("SavedWorkspace/workspace.ser");

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
                    addWorkspace(workspace, sc);
                    break;
                case STEP:
                    stepIntoWorkspace(workspace, sc);
                    break;
                case MOVE:
                    moveIntoWorkspace(workspace, sc);
                    break;
                case PRINT:
                    System.out.println(workspace);
                    break;
                case SAVE:
                    saveWorkspace(workspace, "SavedWorkspace/workspace.ser");
                    break;
                case DELETE:
                    deleteWorkspace(workspace, sc);
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
     * Adds a workspace into the the workspace passed in.
     *
     * @param workspace The parent task
     * @param sc        Scanner to read input from
     */
    private static void addWorkspace(final WorkspaceManager workspace, final Scanner sc) {
        System.out.print("Enter your subtask: ");
        String name = sc.nextLine();
        System.out.print("Enter your subtask type (Task or Action): ");
        String type = sc.nextLine();
        workspace.addWorkspace(name, type);
    }
    /**
     * Given a workspace, give the option to the user to step up into its parent workspace
     * or into one of its sub tasks if there are any.
     *
     * @param workspace Current workspace
     * @param sc        Scanner to read input from
     * @return          The workspace the user steps into
     */
    private static boolean stepIntoWorkspace(final WorkspaceManager workspace, final Scanner sc) {
        System.out.println("0 " + workspace.getParent().get("Name"));
        ArrayList<Map<String, String>> act = workspace.getTasks();
        for (int i = 0; i < act.size(); i++) {
            System.out.println((i + 1) + " " + act.get(i).get("Name"));
        }
        System.out.print("Choose a task to step into: ");
        int i = Integer.parseInt(sc.nextLine());
        if (i == 0) {
            workspace.stepUp();
        } else {
            try {
                workspace.stepIntoWorkspace(i - 1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(i + " is not a valid option");
            }
        }
        return true;
    }
    /**
     * Prompts user to first find the workspace they want to move the currentWorkspace into. The
     * workspace manager then puts it into that workspace based on it's absolute path from root.
     *
     * @param workspace WorkspaceManager
     * @param sc        Scanner for input
     */
    private static void moveIntoWorkspace(final WorkspaceManager workspace, final Scanner sc) {
        ArrayList<Integer> pos = new ArrayList<>();
        String inp = "RANDOM";
        ArrayList<Map<String, String>> details = workspace.taskDetailsOf(new ArrayList<Integer>(0));

        while (!inp.equals("")) {
            for (int i = 0; i < details.size(); i++) {
                System.out.println(i + " " + details.get(i).get("Name"));
            }
            System.out.print("Choose a task to move to (hit enter to select current task): ");
            try {
                inp = sc.nextLine();
                int i = Integer.parseInt(inp);
                pos.add(i);
            } catch (Exception e) {
                System.out.println("Invalid input -- " + inp);
            }
            details = workspace.taskDetailsOf(pos);
        }
        workspace.moveCurrentWorkspace(pos);
    }
    /**
     * Displays list of workspaces in current Tasks list. Prompts the user to delete one. It then
     * removes that workspace.
     *
     * @param workspace WorkspaceManager responsibe for deleting workspace
     * @param sc        Scanner used for input
     */
    private static void deleteWorkspace(final WorkspaceManager workspace, final Scanner sc) {
        System.out.println("Choose workspace to delete: ");
        ArrayList<Map<String, String>> act = workspace.getTasks();
        for (int i = 0; i < act.size(); i++) {
            System.out.println((i + " " + act.get(i).get("Name")));
        }
        int i = Integer.parseInt(sc.nextLine());
        workspace.deleteWorkspace(i);
    }
    /**
     * Save the workspace.
     *
     * @param   w       The workspace to save
     * @param   path    The path to save workspace to
     * @return          True if save was successful, false otherwise
     */
    public static boolean saveWorkspace(final WorkspaceManager w, final String path) {
        return w.save(path);
    }

    /**
     * Load in the workspace in the given file.
     *
     * @param file  Path to workspace on disk
     * @return      The WorkspaceManager that manages the loaded workspace
     */
    public static WorkspaceManager loadWorkspace(final String file) {
        return WorkspaceManager.loadWorkspace(file);
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
}
