package simpletask.main.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import simpletask.main.entities.WorkspaceManager;

/**
 * Temp class to test GUI.
 *
 * @author Matthew Taggart
 */
public class TestGUI extends Application {
    @Override
    public void start(final Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../gui/resources/Workspace.fxml"));
        primaryStage.setTitle("Simple Task");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }
    /**
     * On initialisation, the app needs to load in the WorkspaceManager from the saved location.
     * This sets up the singleton instance.
     */
    @Override
    public void init() throws Exception {
        // Load the saved data into the WorkspaceManager
        WorkspaceManager.loadWorkspace("SavedWorkspace/workspace.ser");
    }
    /**
     * The WorkdspaceManager should be saved on exit.
     */
    @Override
    public void stop() throws Exception {
        // WorkspaceManager.getInstance().save("SavedWorkspace/workspace.ser");
    }

    public static void main(final String[] args) {
        launch(args);
    }
}