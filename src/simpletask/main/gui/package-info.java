/**
 * Contains all the classes and resources for the JavaFX GUI to work. The Manager class
 * is a singleton class used for sharing and managing the the GUI representaion of the
 * workspace. It does this by interacting with the WorkspaceManager.
 * <p>
 * The main window is maintained by the MainController class. The CardController is used
 * when creating new tasks. It will store the user data then pass it onto the Manager
 * and WorkspaceManager.
 */
package simpletask.main.gui;