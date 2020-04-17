package simpletask.app;

import simpletask.entities.Task;
import simpletask.entities.Options;
import simpletask.entities.Workspace;

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
        //TODO This part will be handled better in the future
        // Create your initial workspace
        System.out.print("Enter the name of your task: ");
        Workspace workspace = new Task(sc.nextLine());

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

            //TODO Refactor the blocks into functions
            switch (option) {
                case ADD: {
                    System.out.print("Enter your subtask: ");
                    workspace.createWorkspace(sc.nextLine());
                    break;
                }
                case MOVE: {
                    System.out.println("0 " + workspace.getParent().getName());
                    ArrayList<Workspace> act = workspace.getTasks();
                    for (int i = 0; i < act.size(); i++) {
                        System.out.println((i + 1) + " " + act.get(i).getName());
                    }
                    System.out.print("Choose a task to move into: ");
                    int i = Integer.parseInt(sc.nextLine());
                    if (i == 0) {
                        workspace = workspace.getParent();
                    } else {
                        try {
                            workspace = act.get(i - 1);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println(i + " is not a valid option");
                        }
                    }
                    break;
                }
                case PRINT: {
                    System.out.println(workspace);
                    break;
                }
                case QUIT:System.out.println("Goodbye"); sc.close(); break;
                case DONOTHING:break;
                default:break;
            }
        } while (option != Options.QUIT);
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
