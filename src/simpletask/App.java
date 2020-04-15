package simpletask;

import entities.Task;
import entities.Actions;
import entities.Action;

import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private final static boolean DEBUG = true;

    private final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        //TODO: This part will be handled better in the future
        // Create your initial workspace
        System.out.print("Enter the name of your task: ");
        Action workspace = new Task(sc.nextLine());

        Actions action;
        String st;
        do {
            displayOptions();
            System.out.println("What would you like to do?");
            st = sc.nextLine().toUpperCase();
            try {
                action = Actions.valueOf(st);
            } catch (IllegalArgumentException e) {
                System.out.println(st + " is an invalid option. Try again");
                action = Actions.DONOTHING;
            }

            //TODO: Refactor body of cases into statements
            switch(action) {
                case ADD:{
                    System.out.print("Enter your subtask: ");
                    workspace.createAction(sc.nextLine());
                    break;
                }
                case MOVE:{
                    System.out.println("0 " + workspace.getParent().getName());
                    ArrayList<Action> act = workspace.getTasks();
                    for(int i = 0; i < act.size(); i++) {
                        System.out.println((i+1) + " " + act.get(i).getName());
                    }
                    System.out.print("Choose a task to move into: ");
                    int i = Integer.parseInt(sc.nextLine());
                    if(i == 0) {
                        workspace = workspace.getParent();
                    } else {
                        try {
                            workspace = act.get(i-1);
                        } catch(IndexOutOfBoundsException e) {
                            System.out.println(i + " is not a valid option");
                        }
                    }
                    break;
                }
                case PRINT:{
                    System.out.println(workspace);
                    break;
                }
                case QUIT:System.out.println("Goodbye");break;
                case DONOTHING:break;
            }
        } while(action != Actions.QUIT);
    }

    private static void displayOptions() {
        System.out.println();
        for(Actions a: Actions.values()) {
            if(a != Actions.DONOTHING) {
                System.out.println(a);
            }
        }
        System.out.println();
    }

    private static void debugLog(String msg) {
        if(DEBUG) {
            System.out.println("[Debug] - " + msg);
        }
    }
}
