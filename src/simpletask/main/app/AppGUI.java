package simpletask.main.app;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simpletask.main.entities.WorkspaceManager;

/**
 * Runs the GUI. It extends the Application class from the JavaFX package. It's main purpose is to
 * initialise the primary stage and ensure it is tidied up when the application is shut.
 *
 * @author Matthew Taggart
 * @see    <a href="https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html">Application</a>
 */
public class AppGUI extends Application {
    /**
     * Width of the window on initialisation.
     */
    public static final int WIDTH = 1200;
    /**
     * Length of the window on initialisation.
     */
    public static final int LENGTH = 800;
    /**
     * This is a reference to the current primaryStage. This class maintains the primary stage
     * and allows access/modification via API's.
     */
    private static Stage primaryStage;
    /**
     * The Config that the application will be using. It will look for the config file (".config") in
     * the current dirctory and build an instance off that.
     */
    private static Config config;
    /**
     * Returns the primaryStage. Another class may need access to the stage to modify or set
     * it's root. Giving the class direct access in this way means that the AppGUI class does not
     * need to have an API for each action you can do to a Stage.
     *
     * @return  The current primaryStage
     */
    public static Stage getStage() {
        return primaryStage;
    }
    /**
     * Sets the primaryStage to the inputted Stage. Used when moving up or down a level in
     * the workspace.
     *
     * @param newPrimaryStage  The stage to set the primaryStage to
     */
    private void setStage(final Stage newPrimaryStage) {
        AppGUI.primaryStage = newPrimaryStage;
    }
    /**
     * Called when the application starts. If there was no .config file found (i.e. on initial startup),
     * it will open the file explorer and ask the user to set the dir in which they will have they're workspace.
     * <p>
     * It then goes on to set the primaryStage as the stage whose root is got from Workspace.fxml. The title and
     * size of the window are also set.
     * <p>
     * If the user does not select a workspace, the application closes.
     */
    @Override
    public void start(final Stage newPrimaryStage) throws Exception {
        // Check if config has been set
        if (null == config.getConfig(ConfigKeys.DIR)) {
            // Show file explorer and configure .config
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.setTitle("Select File To Save your Workspace");
            File workspace = fileChooser.showSaveDialog(newPrimaryStage);
            if (null == workspace) {
                System.exit(0);     // Exit if user has not entered a workspace
            }
            // Initialise the new workspace and save the config
            WorkspaceManager.initialise(workspace.getName());
            WorkspaceManager.getInstance().save(workspace.getAbsolutePath());
            config.setValue(ConfigKeys.DIR, workspace.getAbsolutePath());
            config.saveCurrentSettings();
        }
        // Set up the Stage to be shown to the user
        Parent root = FXMLLoader.load(getClass().getResource("../gui/resources/Workspace.fxml"));
        setStage(newPrimaryStage);
        newPrimaryStage.setTitle("Simple Task");
        newPrimaryStage.setScene(new Scene(root, WIDTH, LENGTH));
        newPrimaryStage.show();
    }
    /**
     * On initialisation, the app needs to load in the WorkspaceManager from the saved location.
     * This sets up the singleton instance. The saved location is got from the .config file. If it
     * does not exist, it will be handled in the start method.
     * <p>
     * The reason this case is handled in the start method is because the FileChooser class needs a
     * stage. But a stage is not created until the start method is run. Trying to create a stage at
     * this point will throw an error as it will not be in the FX application thread (it will
     * be the JavaFX-Launcher thread).
     */
    @Override
    public void init() throws Exception {
        config = new Config(".config");
        // Load the saved data into the WorkspaceManager
        if (null != config.getConfig(ConfigKeys.DIR)) {
            WorkspaceManager.loadWorkspace(config.getConfig(ConfigKeys.DIR));
        }
    }
    /**
     * The WorkdspaceManager should be saved on exit.
     */
    @Override
    public void stop() throws Exception {
        WorkspaceManager.getInstance().save(config.getConfig(ConfigKeys.DIR));
    }
    /**
     * The main method simply calls the launch method which is from the Application class.
     *
     * @param   args    The arguments passed into the program. Currently empty
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
